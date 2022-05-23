package com.impulse.impulse.activity

import android.os.Bundle
import com.impulse.impulse.R
import com.impulse.impulse.databinding.ActivitySignInBinding
import com.impulse.impulse.fragment.PhoneNumberFragment

/*
* In this activity user can sign in our application or go to SignUp page
* */
class SignInActivity : BaseActivity() {
    private val TAG = SignInActivity::class.java.toString()
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        val phoneNumberFragment = PhoneNumberFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.flSignIn, phoneNumberFragment)
            .commit()
    }
}