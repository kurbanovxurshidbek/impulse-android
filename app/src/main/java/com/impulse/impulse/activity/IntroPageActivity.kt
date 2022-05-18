package com.impulse.impulse.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.impulse.impulse.R
import com.impulse.impulse.adapter.IntroPageItemAdapter
import com.impulse.impulse.databinding.ActivityIntroPageBinding
import com.impulse.impulse.manager.PrefsManager
import com.impulse.impulse.model.IntroPageItem

/*
* This activity only will be showed first time users
* */
class IntroPageActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroPageBinding
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            viewPager.adapter = IntroPageItemAdapter(context, getItems())
            dotsIndicator.setViewPager2(viewPager)
            btnContinue.setOnClickListener {
                viewPager.currentItem = ++viewPager.currentItem
            }
            tvSkip.setOnClickListener {
                viewPager.currentItem = getItems().size - 1
            }
            btnGetStarted.setOnClickListener {
                saveLoggedState()
                callMainActivity(this@IntroPageActivity)
                finish()
            }
        }
        applyPageStateChanges()
    }

    private fun saveLoggedState() {
        isFirstTime = false
        PrefsManager.getInstance(context)!!.setFirstTime("isFirstTime", isFirstTime)
    }

    private fun applyPageStateChanges() {
        binding.apply {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (position == getItems().size - 1) {
                        btnContinue.visibility = View.GONE
                        btnGetStarted.visibility = View.VISIBLE
                    } else {
                        btnContinue.visibility = View.VISIBLE
                        btnGetStarted.visibility = View.GONE
                    }

                }
            })
        }
    }

    private fun getItems(): ArrayList<IntroPageItem> {
        val items = ArrayList<IntroPageItem>()
        items.add(
            IntroPageItem(
                R.drawable.im_time_management,
                getString(R.string.str_save_your_time),
                getString(R.string.str_description)
            )
        )
        items.add(
            IntroPageItem(
                R.drawable.im_medic,
                getString(R.string.str_advice_doctors),
                getString(R.string.str_description)
            )
        )
        items.add(
            IntroPageItem(
                R.drawable.im_ac_support,
                getString(R.string.str_active_support),
                getString(R.string.str_description)
            )
        )
        return items
    }
}


