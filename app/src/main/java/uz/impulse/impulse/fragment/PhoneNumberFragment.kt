package uz.impulse.impulse.fragment

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.impulse.impulse.R
import uz.impulse.impulse.data.remote.PhoneNumberHttp
import uz.impulse.impulse.data.remote.service.PhoneNumberService
import uz.impulse.impulse.databinding.FragmentPhoneNumberBinding
import uz.impulse.impulse.manager.PrefsManager
import uz.impulse.impulse.model.PhoneResponse
import uz.impulse.impulse.utils.Extensions.toast
import uz.impulse.impulse.utils.Logger
import uz.impulse.impulse.viewmodel.ContactsViewModel
import uz.impulse.impulse.viewmodel.PhoneAuthViewModel
import uz.impulse.impulse.viewmodel.factory.ContactsViewModelFactory
import uz.impulse.impulse.viewmodel.factory.PhoneAuthViewModelFactory
import uz.impulse.impulse.viewmodel.repository.ContactRepository
import uz.impulse.impulse.viewmodel.repository.MessageRepository
import uz.impulse.impulse.viewmodel.repository.PhoneAuthRepository

class PhoneNumberFragment : BaseFragment() {
    private var _binding: FragmentPhoneNumberBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isChecked = false
    private var fullPhoneNumber = ""

    private lateinit var viewModel: PhoneAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.chbTerms.isChecked = false
    }

    private fun initViews() {
        setupViewModel()
        changeLLColor()
        changeTvColor()

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

            mainPhoneNumber.setOnClickListener {
                hideKeyboard(etPhoneNumber)
            }

            btnContinue.setOnClickListener {
                vibrate()
                fullPhoneNumber = ccp.fullNumberWithPlus
                savePhoneNumberToSharePref()
                if (isFilledPhone()) {
                    openConfirmationPage()
                }
                viewModel.requestConfirmationCode(fullPhoneNumber)
                viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        Logger.d("@@@", response.body()!!.message.toString())
                        Logger.d("@@@", response.code().toString())
                    } else {
                        toast(response.code().toString())
                    }
                })
            }
            tvTermsOfUse.setOnClickListener {
                // openTermsOfUse
            }
        }
    }

    private fun savePhoneNumberToSharePref() {
        val manager = PrefsManager.getInstance(requireContext())
        val key = "phoneNumber"
        if (manager!!.getData(key) != null) {
            manager.deleteData(key)
        }
        manager.saveData(key, fullPhoneNumber)
    }

    /*
    *  This function changes the spanned text color in textview
    * */
    private fun changeTvColor() {
        binding.apply {
            val spannableString = SpannableString(getString(R.string.str_term_of_use))
            spannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.main_red)),
                0,
                21,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tvTermsOfUse.text = spannableString
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
            .replace(R.id.flSignIn, ConfirmationFragment())
            .addToBackStack(null)
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            PhoneAuthViewModelFactory(
                PhoneAuthRepository(
                    PhoneNumberHttp.createServiceWithAuth(
                        PhoneNumberService::class.java
                    )
                )
            )
        )[PhoneAuthViewModel::class.java]
    }
}