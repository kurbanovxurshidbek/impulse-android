package com.impulse.impulse.activity

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.impulse.impulse.R
import com.impulse.impulse.databinding.ActivitySignInBinding

/*
* In this activity user can sign in our application or go to SignUp page
* */
class SignInActivity : BaseActivity() {
    private val TAG = SignInActivity::class.java.toString()
    private lateinit var binding: ActivitySignInBinding

    private var isChecked = false
    private var fullPhoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        changeLLColor()

        binding.apply {
            etCountryCode.setText("+${ccp.selectedCountryCode}", TextView.BufferType.EDITABLE)
            ccp.setOnCountryChangeListener {
                etCountryCode.setText("+${ccp.selectedCountryCode}", TextView.BufferType.EDITABLE)
            }
            ccp.registerCarrierNumberEditText(etPhoneNumber)

            chbTerms.setOnClickListener {
                if (it is CheckBox) {
                    isChecked = it.isChecked
                }
            }

            btnContinue.setOnClickListener {
                fullPhoneNumber = ccp.fullNumberWithPlus

            }
        }
    }

    private fun isFilledPhone(): Boolean {
        return binding.etPhoneNumber.text!!.isNotEmpty()
    }

    private fun isCheckedTerms(view: View?): Boolean {
        return if (isChecked) {
            view!!.isEnabled = true
            true
        } else {
            view!!.isEnabled = false
            Toast.makeText(this@SignInActivity, "Please read terms of use", Toast.LENGTH_SHORT)
                .show()
            false
        }
    }

    /*
    *   This function can change linear layout color to underline color
    * */
    private fun changeLLColor() {
        binding.apply {
            etCountryCode.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    llPhoneCode.setBackgroundColor(resources.getColor(R.color.main_red))
                } else {
                    llPhoneCode.setBackgroundColor(resources.getColor(R.color.ll_bg_color))
                }
            }

            etPhoneNumber.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    llPhoneNumber.setBackgroundColor(resources.getColor(R.color.main_red))
                } else {
                    llPhoneNumber.setBackgroundColor(resources.getColor(R.color.ll_bg_color))
                }
            }
        }
    }
}