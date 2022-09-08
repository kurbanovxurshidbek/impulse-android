package uz.impulse.impulse.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import uz.impulse.impulse.R
import uz.impulse.impulse.databinding.FragmentRegNameBinding
import uz.impulse.impulse.manager.PrefsManager

class RegNameFragment : BaseFragment() {
    private var _binding: FragmentRegNameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegNameBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        fioFocusListener()
        passportFocusListener()
        binding.mainRegName.setOnClickListener {
            closeKeyboards()
        }
        binding.btnContinue.setOnClickListener {
            vibrate()
            PrefsManager.getInstance(requireContext())!!.setBoolean("isFilledName", true)
            callMainActivity(requireActivity())
        }
    }

    private fun fioFocusListener() {
        binding.apply {
            etFio.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (etFio.text!!.isEmpty()) {
                        etFio.error = getString(R.string.str_fio_error)
                    }
                    true
                } else {
                    false
                }
            }
            etFio.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    if (etFio.text!!.isEmpty()) {
                        fioContainer.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.main_red)))
                        fioContainer.helperText = getString(R.string.str_fio_error)
                    }
                }
            }
            etFio.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.isNotEmpty()) {
                        fioContainer.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.helper_text_color)))
                        fioContainer.helperText = getString(R.string.str_fio_helper)
                    } else {
                        fioContainer.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.main_red)))
                        fioContainer.helperText = getString(R.string.str_fio_error)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    private fun passportFocusListener() {
        binding.apply {
            etPassport.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    if (etPassport.text!!.isEmpty()) {
                        passportContainer.setHelperTextColor(
                            ColorStateList.valueOf(
                                resources.getColor(
                                    R.color.main_red
                                )
                            )
                        )
                        passportContainer.helperText = getString(R.string.str_passport_error)
                    }
                }
            }
            etPassport.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0!!.isNotEmpty()) {
                        passportContainer.setHelperTextColor(
                            ColorStateList.valueOf(
                                resources.getColor(
                                    R.color.helper_text_color
                                )
                            )
                        )
                        passportContainer.helperText = getString(R.string.str_passport_helper)
                    } else {
                        passportContainer.setHelperTextColor(
                            ColorStateList.valueOf(
                                resources.getColor(
                                    R.color.main_red
                                )
                            )
                        )
                        passportContainer.helperText = getString(R.string.str_passport_error)
                    }

                    btnContinue.isEnabled =
                        etFio.text!!.isNotEmpty() && etPassport.text!!.isNotEmpty()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
    }

    private fun closeKeyboards() {
        binding.apply {
            hideKeyboard(etFio)
            hideKeyboard(etPassport)
        }
    }
}