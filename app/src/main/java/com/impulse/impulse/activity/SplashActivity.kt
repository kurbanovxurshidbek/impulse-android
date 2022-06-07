package com.impulse.impulse.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
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
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        hideSystemUI()
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
                val manager = PrefsManager.getInstance(context)!!
                if (manager.isFirstTime("isFirstTime")) {
                    callIntroPageActivity(this@SplashActivity)
                    finish()
                } else if (manager.isLoggedIn("isLoggedIn")) {
                    if (manager.isFilledName("isFilledName")) {
                        callMainActivity(context)
                        finish()
                    } else {
                        callSignUpActivity(context)
                        finish()
                    }
                } else {
                    callSignInActivity(this@SplashActivity)
                    finish()
                }
            }
        }.start()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Hide nav bar
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide status bar
                // View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // layout Behind nav bar
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // layout Behind status bar
                )
    }
}