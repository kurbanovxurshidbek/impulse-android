package com.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.impulse.impulse.R
import com.impulse.impulse.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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
        binding.apply {
            tName.text = "name"
            tAge.text = "20 years"
            tWeight.text = "176 cm"
            tHeight.text = "58 kg"
            tBloodtype.text = "Orh+"

            ivEdit.setOnClickListener {
                openEditProfileFragment()
            }
        }
    }

    private fun openEditProfileFragment() {
        navController.navigate(R.id.profileToEditProfileFragment)
    }
}