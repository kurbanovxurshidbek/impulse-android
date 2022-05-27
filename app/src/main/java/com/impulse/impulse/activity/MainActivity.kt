package com.impulse.impulse.activity

import android.os.Bundle
import android.widget.EditText
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.viewpager.widget.ViewPager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.ViewPagerAdapter
import com.impulse.impulse.databinding.ActivityMainBinding
import com.impulse.impulse.fragment.ContactsFragment
import com.impulse.impulse.fragment.FirstAidFragment
import com.impulse.impulse.fragment.HomeFragment
import com.impulse.impulse.fragment.ProfileFragment

class MainActivity : BaseActivity() {
    private val TAG = MainActivity::class.java.toString()
    private var index = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val insetsCompat = toWindowInsetsCompat(insets, view)
            binding.bottomNavigationView.isGone = insetsCompat.isVisible(ime())
            view.onApplyWindowInsets(insets)
        }

        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> viewPager.currentItem = 0
                    R.id.navigation_contacts -> viewPager.currentItem = 1
                    R.id.navigation_firstAid -> viewPager.currentItem = 2
                    R.id.navigation_profile -> viewPager.currentItem = 3
                }
                true
            }

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    index = position
                    bottomNavigationView.menu.getItem(index).isChecked = true
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            setUpViewPager(viewPager)
        }
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(ContactsFragment())
        adapter.addFragment(FirstAidFragment())
        adapter.addFragment(ProfileFragment())
        viewPager!!.adapter = adapter
    }

}