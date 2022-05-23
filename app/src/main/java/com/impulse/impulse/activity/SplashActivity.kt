package com.impulse.impulse.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.impulse.impulse.R
import com.impulse.impulse.databinding.ActivitySplashBinding
import com.impulse.impulse.manager.PrefsManager

/*
* In SplashActivity user can visit to SignInActivity or MainActivity
*/
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val TAG = SplashActivity::class.java.toString()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        Glide.with(context).load(R.drawable.branding).into(binding.ivGif)
        countDownTimer()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2200, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                if (PrefsManager.getInstance(context)!!.isFirstTime("isFirstTime")) {
                    callIntroPageActivity(this@SplashActivity)
                    finish()
                } else {
                    callMainActivity(this@SplashActivity)
//                    callSignInActivity(this@SplashActivity)
                    finish()
                }
            }
        }.start()
    }
}