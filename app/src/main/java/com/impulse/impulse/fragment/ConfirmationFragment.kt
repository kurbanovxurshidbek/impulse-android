package com.impulse.impulse.fragment

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.impulse.impulse.R
import com.impulse.impulse.databinding.FragmentConfirmationBinding
import com.impulse.impulse.manager.PrefsManager

class ConfirmationFragment : BaseFragment() {
    private var _binding: FragmentConfirmationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var code: String = ""
    private var hasFilled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        codeConfirmation()
        changeTvColor()
        setPhoneNumber()
        binding.apply {
            mainConfirmation.setOnClickListener {
                closeKeyboards()
            }
            btnContinue.setOnClickListener {
                // checkCode()
                PrefsManager.getInstance(requireContext())!!.setBoolean("isLoggedIn", true)
                callSignUpActivity(requireActivity())
            }
        }
    }


    private fun setPhoneNumber() {
        val manager = PrefsManager.getInstance(requireContext())
        binding.apply {
            tvPhoneNumber.text = getString(R.string.str_code_sent, manager!!.getData("phoneNumber"))
        }
    }

    /*
    *  This function changes the spanned text color in textview
    * */
    private fun changeTvColor() {
        binding.apply {
            val spannableString = SpannableString(getString(R.string.str_hasnt_get_code))
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.main_red)),
                27,
                41,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvResend.text = spannableString
        }
    }

    private fun codeConfirmation() {
        binding.apply {
            etCode1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    etCode2.requestFocus()
                    if (p0!!.length > 1) {
                        etCode1.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCode2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    etCode3.requestFocus()
                    if (p0!!.length > 1) {
                        etCode2.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCode3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    etCode4.requestFocus()
                    if (p0!!.length > 1) {
                        etCode3.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCode4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    etCode5.requestFocus()
                    if (p0!!.length > 1) {
                        etCode4.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCode5.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    etCode6.requestFocus()
                    if (p0!!.length > 1) {
                        etCode5.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            etCode6.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.length > 1) {
                        etCode6.setText(p0[0].toString(), TextView.BufferType.EDITABLE)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    closeKeyboards()
                    getCode()
                    checkHasFilled()
                }
            })
        }
    }

    private fun checkHasFilled() {
        binding.apply {
            hasFilled = etCode6.text!!.isNotEmpty()
            if (hasFilled) {
                btnContinue.isEnabled = hasFilled
            } else {
                btnContinue.isEnabled = false
            }
        }
    }


    private fun getCode() {
        binding.apply {
            code = etCode1.text.toString() + etCode2.text.toString() + etCode3.text.toString() +
                    etCode4.text.toString() + etCode5.text.toString() + etCode6.text.toString()
        }
        Log.d("@@@", "codeConfirmation: $code")
    }

    private fun closeKeyboards() {
        binding.apply {
            hideKeyboard(etCode1)
            hideKeyboard(etCode2)
            hideKeyboard(etCode3)
            hideKeyboard(etCode4)
            hideKeyboard(etCode5)
            hideKeyboard(etCode6)
        }
    }
}