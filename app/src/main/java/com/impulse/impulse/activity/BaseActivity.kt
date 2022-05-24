package com.impulse.impulse.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
}