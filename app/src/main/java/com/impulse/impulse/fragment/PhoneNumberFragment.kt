package com.impulse.impulse.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.impulse.impulse.R
import com.impulse.impulse.databinding.FragmentPhoneNumberBinding

class PhoneNumberFragment : BaseFragment() {
    private var _binding: FragmentPhoneNumberBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isChecked = false
    private var fullPhoneNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
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
                    checkForTermsOfUse(it)
                }
            }

            btnContinue.setOnClickListener {
                fullPhoneNumber = ccp.fullNumberWithPlus
                if (isFilledPhone()) {
                    openConfirmationPage()
                }
            }
        }
    }

    private fun checkForTermsOfUse(checkBox: CheckBox) {
        binding.apply {
            isChecked = checkBox.isChecked
            if (isFilledPhone()) {
                btnContinue.isEnabled = isChecked
            } else {
                btnContinue.isEnabled = false
            }
        }
    }

    private fun openConfirmationPage() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.flMain, ConfirmationFragment())
            .commit()
    }

    private fun isFilledPhone(): Boolean {
        return binding.etPhoneNumber.text!!.isNotEmpty()
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
            etPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (chbTerms.isChecked) {
                        btnContinue.isEnabled = chbTerms.isChecked
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (!isFilledPhone()) {
                        btnContinue.isEnabled = isFilledPhone()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}