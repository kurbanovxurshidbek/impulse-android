package com.impulse.impulse.activity

import android.os.Bundle
import com.impulse.impulse.R
import com.impulse.impulse.fragment.PhoneNumberFragment
import com.impulse.impulse.fragment.RegNameFragment

/*
* in this activity user can register account for application
* */
class SignUpActivity : BaseActivity() {
    private val TAG = SignUpActivity::class.java.simpleName.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        val regNameFragment = RegNameFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.flSignUp, regNameFragment)
            .commit()
    }
}