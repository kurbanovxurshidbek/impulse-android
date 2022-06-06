package com.impulse.impulse.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.HomeItemAdapter
import com.impulse.impulse.databinding.DialogHomeViewBinding
import com.impulse.impulse.databinding.FragmentHomeBinding
import com.impulse.impulse.manager.PrefsManager
import com.impulse.impulse.model.HomeItem
import com.impulse.impulse.utils.SpacesItemDecoration

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

    private fun initViews() {
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = HomeItemAdapter(requireContext(), getAllItems())
            val decoration = SpacesItemDecoration(15)
            recyclerView.addItemDecoration(decoration)


            btnCall.setOnLongClickListener {
                var dialogChosenOption: String? = null
                val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                val dialogBinding = DialogHomeViewBinding.inflate(layoutInflater)
                builder.setView(dialogBinding.root)
                val dialog = builder.create()

                dialogBinding.btnOk.setOnClickListener {
                    dialogBinding.apply {
                        if (rgHome.checkedRadioButtonId == -1) {
                            //
                        } else {
                            val checkedId = rgHome.checkedRadioButtonId
                            val radioButton = rgHome.findViewById<RadioButton>(checkedId)
                            val idx = rgHome.indexOfChild(radioButton)

                            val r = rgHome.getChildAt(idx) as RadioButton
                            dialogChosenOption = r.text.toString()
                            Log.d("@@@", "Home Dialog option : $dialogChosenOption")
                        }
                    }
                    if (dialogChosenOption != null) {
                        btnCall.playAnimation()
                        Log.d("@@@", "Home Dialog option : $dialogChosenOption")
                    }
                    dialog.dismiss()
                }

                dialogBinding.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()


                dialogBinding.tvLaw.movementMethod = LinkMovementMethod.getInstance()

                true
            }

            tvName.text = getString(
                R.string.str_hello_name,
                PrefsManager.getInstance(requireContext())!!.getData("userName")
            )
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