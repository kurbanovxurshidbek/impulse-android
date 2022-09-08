package uz.impulse.impulse.fragment

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
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import uz.impulse.impulse.R
import uz.impulse.impulse.databinding.FragmentConfirmationBinding
import uz.impulse.impulse.manager.PrefsManager
import uz.impulse.impulse.utils.Extensions.toast
import uz.impulse.impulse.utils.Logger
import java.util.concurrent.TimeUnit

class ConfirmationFragment : BaseFragment() {
    private val TAG = ConfirmationFragment::class.java.simpleName
    private var _binding: FragmentConfirmationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var code: String = ""
    private var hasFilled = false

    // firebase auth part below
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mVerificationId: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var phoneNumber: String
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken

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
        firebaseAuth = FirebaseAuth.getInstance()
        phoneNumber = PrefsManager.getInstance(requireContext())!!.getData("phoneNumber")!!
        codeConfirmation()
        changeTvColor()
        setPhoneNumber()

        setAuth()
        binding.apply {
            mainConfirmation.setOnClickListener {
                closeKeyboards()
            }
            btnContinue.setOnClickListener {

            }
        }
    }

    private fun setAuth() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "onVerificationFailed: ${e.message} ")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.d(TAG, "onCodeSent: $verificationId")
                mVerificationId = verificationId
                forceResendingToken = token
//                pd.dismiss()

                Log.d(TAG, "Verification code sent...")
            }
        }

        startPhoneVerification(phoneNumber)
        binding.btnContinue.setOnClickListener {
            verifyPhoneWithCode(mVerificationId, code)
        }
        binding.tvResend.setOnClickListener {
            resendVerificationCode(phoneNumber, forceResendingToken)
        }
    }

    private fun startPhoneVerification(phoneNumber: String) {
//        pd.setMessage("Verifying phone number....")
//        pd.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneWithCode(id: String, code: String) {
//        pd.setMessage("Verifying code .........")
//        pd.show()

        val credential = PhoneAuthProvider.getCredential(id, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        pd.setMessage("Logging in...")
//        pd.show()

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                vibrate()
                PrefsManager.getInstance(requireContext())!!.setBoolean("isLoggedIn", true)
                callSignUpActivity(requireActivity())

//                pd.dismiss()
                val phone = firebaseAuth.currentUser?.phoneNumber
                Log.d(TAG, "Logged in as $phone")
            }
            .addOnFailureListener {
//                pd.dismiss()
                toast("Tasdiqlash kodi xato")
                Log.d(TAG, "Error ${it.message}")
            }
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
//        pd.setMessage("Verifying phone number....")
//        pd.show()

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setForceResendingToken(token)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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
            Logger.d("@@@", "Code : $code")
        }
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