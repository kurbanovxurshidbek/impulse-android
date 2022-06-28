package com.impulse.impulse.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.impulse.impulse.utils.Logger

open class BaseActivity : AppCompatActivity() {
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    fun callMainActivity(context: Context) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun callIntroPageActivity(context: Context) {
        val intent = Intent(this, IntroPageActivity::class.java)
        startActivity(intent)
    }

    fun callSignInActivity(context: Context) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun callSignUpActivity(context: Context) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    open fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
}