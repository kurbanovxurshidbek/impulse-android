package com.impulse.impulse.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.HomeItemAdapter
import com.impulse.impulse.databinding.DialogHomeViewBinding
import com.impulse.impulse.databinding.FragmentHomeBinding
import com.impulse.impulse.manager.PrefsManager
import com.impulse.impulse.model.HomeItem
import com.impulse.impulse.utils.SpacesItemDecoration
import com.impulse.impulse.utils.Extensions.toast
import com.impulse.impulse.utils.Logger

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var prefsManager: PrefsManager
    private lateinit var navController: NavController

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun initViews() {
        prefsManager = PrefsManager.getInstance(requireContext())!!
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)
            val decoration = SpacesItemDecoration(10)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = HomeItemAdapter(this@HomeFragment, getAllItems())


            Logger.d("@@@", "${prefsManager.hasCalled("hasCalled")}")
            btnCall.setOnLongClickListener {
                if (!prefsManager.hasCalled("hasCalled")) {
                    setDialog()
                } else {
                    toast(getString(R.string.str_has_called))
                }
                true
            }

            btnCall.setOnClickListener {
                toast(getString(R.string.str_press_and_hold))
            }

            tvName.text = getString(
                R.string.str_hello_name,
                PrefsManager.getInstance(requireContext())!!.getData("userName")
            )

            ivProfile.setOnClickListener {
                navController.navigate(R.id.homeToProfileFragment)
            }

            ivLocation.setOnClickListener {
                navController.navigate(R.id.homeToCurrentLocationFragment)
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
                    Log.d("@@@", "Home Dialog option : $dialogChosenOption")
                    prefsManager.setBoolean("hasCalled", true)
                    Logger.d("@@@", "${prefsManager.hasCalled("hasCalled")}")
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

    fun openFirstAidFragment() {
        navController.navigate(R.id.homeToFirstAidProfile)
    }

//    private fun openCurrentLocationFragment() {
//        requireActivity()
//            .supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.rootView, CurrentLocationFragment())
//            .addToBackStack("home")
//            .commit()
//    }
}