package com.impulse.impulse.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.HomeItemAdapter
import com.impulse.impulse.databinding.DialogHomeViewBinding
import com.impulse.impulse.databinding.FragmentHomeBinding
import com.impulse.impulse.manager.PrefsManager
import com.impulse.impulse.model.HomeItem
import com.impulse.impulse.utils.Extensions.toast
import com.impulse.impulse.utils.SpacesItemDecoration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var prefsManager: PrefsManager
    private lateinit var navController: NavController
    private var job: Job? = null
    private var clickCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job?.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setBackDoubleBackPressed()
    }

    private fun setBackDoubleBackPressed() {
        var backPressedTime: Long = 0
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 3000 > System.currentTimeMillis()) {
                        activity!!.finish()
                    } else {
                        toast(getString(R.string.str_double_tap_to_exit))
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            })
    }


    private fun initViews() {
        prefsManager = PrefsManager.getInstance(requireContext())!!
        val hasCalled = prefsManager.hasCalled("hasCalled")
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)
            val decoration = SpacesItemDecoration(10)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = HomeItemAdapter(this@HomeFragment, getAllItems())

            btnCall.setOnLongClickListener {
                if (!hasCalled) {
                    setDialog()
                    setTextMainTexts(hasCalled)
                } else {
                    prefsManager.setBoolean("hasCalled", false)
                    toast(getString(R.string.str_has_called))
                }
                true
            }

            btnCall.setOnClickListener {

                job = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                    if (clickCount < 2) {
                        toast(getString(R.string.str_press_and_hold))
                    } else {
                        delay(5000)
                        clickCount = 0
                    }
                }

                clickCount++
            }

            tvName.text = getString(
                R.string.str_hello_name,
                PrefsManager.getInstance(requireContext())!!.getData("userName")
            )

            lottieProfile.setOnClickListener {
                navigateToProfileFragment()
            }

            ivLocation.setOnClickListener {
                navController.navigate(R.id.homeToCurrentLocationFragment)
            }
        }
    }

    private fun setTextMainTexts(hasCalled: Boolean) {
        binding.apply {
            if (!hasCalled) {
                tvNeedAmbulance.text = getString(R.string.str_ambulance_on_way)
                tvHoldButton.text = getString(R.string.str_dont_panic)
            } else {
                tvNeedAmbulance.text = getString(R.string.str_help_needed)
                tvHoldButton.text = getString(R.string.str_hold_button)
            }
        }
    }

    private fun setDialog() {
        binding.apply {
            var dialogChosenOption: String? = null
            val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            val dialogBinding = DialogHomeViewBinding.inflate(layoutInflater)
            builder.setView(dialogBinding.root)
            val dialog = builder.create()


            dialogBinding.apply {
                rbEasy.setOnClickListener { btnOk.isEnabled = true }
                rbMedium.setOnClickListener { btnOk.isEnabled = true }
                rbHard.setOnClickListener { btnOk.isEnabled = true }
                rbHardest.setOnClickListener { btnOk.isEnabled = true }

                btnOk.setOnClickListener {
                    if (rgHome.checkedRadioButtonId == -1) {
                        //
                    } else {
                        val checkedId = rgHome.checkedRadioButtonId
                        val radioButton = rgHome.findViewById<RadioButton>(checkedId)
                        val idx = rgHome.indexOfChild(radioButton)

                        val r = rgHome.getChildAt(idx) as RadioButton
                        dialogChosenOption = r.text.toString()
                    }
                    btnCall.playAnimation()
                    changeAnimations()
                    changeText()
                    Log.d("@@@", "Home Dialog option : $dialogChosenOption")
                    dialog.dismiss()
                }

                tvLaw.setOnClickListener {
                    openLawWithBrowser()
                }
            }


            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()


            dialogBinding.tvLaw.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun changeText() {
        binding.apply {
            if (!prefsManager.hasCalled("hasCalled")) {
                tvNeedAmbulance.text = getString(R.string.str_help_needed)
                tvHoldButton.text = getString(R.string.str_hold_button)
            } else {
                tvNeedAmbulance.text = getString(R.string.str_ambulance_on_way)
                tvHoldButton.text = getString(R.string.str_dont_panic)
            }
        }
    }

    private fun changeAnimations() {
        binding.btnClick.visibility = View.GONE
        binding.btnImpulse.visibility = View.VISIBLE
    }

    private fun openLawWithBrowser() {
        val url = getString(R.string.str_open_law)
        Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse(url)
            startActivity(it)
        }
    }


    private fun getAllItems(): ArrayList<HomeItem> {
        val items = ArrayList<HomeItem>()
        items.add(HomeItem(getString(R.string.str_accident), R.drawable.ic_accident))
        items.add(HomeItem(getString(R.string.str_injury), R.mipmap.ic_injury))
        items.add(HomeItem(getString(R.string.str_feeling_bad), R.mipmap.ic_feeling_bad))
        return items
    }
}