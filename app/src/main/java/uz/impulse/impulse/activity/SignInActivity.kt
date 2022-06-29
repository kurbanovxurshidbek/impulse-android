package uz.impulse.impulse.activity

import android.os.Bundle
import uz.impulse.impulse.R
import uz.impulse.impulse.databinding.ActivitySignInBinding
import uz.impulse.impulse.fragment.PhoneNumberFragment


/*
* In this activity user can sign in our application or go to SignUp page
* */
class SignInActivity : uz.impulse.impulse.activity.BaseActivity() {
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