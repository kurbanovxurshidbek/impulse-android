package com.impulse.impulse.activity

import android.os.Bundle
import android.widget.EditText
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
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
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            val navController = findNavController(R.id.fragmentContainerView)
            bottomNavigationView.setupWithNavController(navController)
        }
    }
}