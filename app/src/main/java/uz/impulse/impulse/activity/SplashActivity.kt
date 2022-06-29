package uz.impulse.impulse.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import uz.impulse.impulse.databinding.ActivitySplashBinding
import uz.impulse.impulse.manager.PrefsManager

/*
* In SplashActivity user can visit to SignInActivity or MainActivity
*/
@SuppressLint("CustomSplashScreen")
class SplashActivity : uz.impulse.impulse.activity.BaseActivity() {
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
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // Hide status bar
                )
    }

}