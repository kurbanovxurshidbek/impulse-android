package com.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.HomeItemAdapter
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
                btnCall.playAnimation()
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