package com.impulse.impulse.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.FirstAidItemAdapter
import com.impulse.impulse.databinding.FragmentContactsBinding
import com.impulse.impulse.databinding.FragmentFirstAidBinding
import com.impulse.impulse.model.FirstAidItem
import com.impulse.impulse.utils.SpacesItemDecoration
import java.lang.RuntimeException

class FirstAidFragment : BaseFragment() {

    private var _binding: FragmentFirstAidBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var listener: HomeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstAidBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        return view
    }

    /*
    * onAttach is for communication of Fragments
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is HomeListener) {
            context
        } else {
            throw RuntimeException("$context must implement ProfileListener")
        }
    }

    /*
    * onDetach is for communication of Fragments
    * */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    listener!!.scrollToHome()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val decoration = SpacesItemDecoration(2)
            recyclerView.addItemDecoration(decoration)

            recyclerView.adapter = FirstAidItemAdapter(getAllItems())
        }
    }

    private fun getAllItems(): List<FirstAidItem> {
        val items = ArrayList<FirstAidItem>()
        items.add(FirstAidItem())
        items.add(FirstAidItem(R.drawable.ic_hypothermia, getString(R.string.str_hypothermia)))
        items.add(FirstAidItem(R.drawable.ic_meningits, getString(R.string.str_meningitis)))
        items.add(FirstAidItem(R.drawable.ic_poison, getString(R.string.str_poisoning)))
        items.add(FirstAidItem(R.drawable.ic_psychological, getString(R.string.str_psychological)))
        items.add(FirstAidItem(R.drawable.ic_epilepsy, getString(R.string.str_seizure)))
        items.add(FirstAidItem(R.drawable.ic_stings, getString(R.string.str_stings)))
        items.add(FirstAidItem(R.drawable.ic_strains, getString(R.string.str_strains)))
        items.add(FirstAidItem(R.drawable.ic_allergies, getString(R.string.str_allergies)))
        items.add(FirstAidItem(R.drawable.ic_stroke, getString(R.string.str_stroke)))
        items.add(FirstAidItem(R.drawable.ic_asthma, getString(R.string.str_asthma_attack)))
        items.add(FirstAidItem(R.drawable.ic_unconscious, getString(R.string.str_unconscious)))
        items.add(FirstAidItem(R.drawable.ic_bleeding, getString(R.string.str_bleeding)))
        items.add(FirstAidItem(R.drawable.ic_broken_bone, getString(R.string.str_broken_bone)))
        items.add(FirstAidItem(R.drawable.ic_burns, getString(R.string.str_burns)))
        items.add(FirstAidItem(R.drawable.ic_choking, getString(R.string.str_choking)))
        items.add(FirstAidItem(R.drawable.ic_diabetic, getString(R.string.str_diabetic_emergency)))
        items.add(FirstAidItem(R.drawable.ic_distress, getString(R.string.str_distress)))
        items.add(FirstAidItem(R.drawable.ic_head_injury, getString(R.string.str_head_injury)))
        items.add(FirstAidItem(R.drawable.ic_heart_attack, getString(R.string.str_heart_attack)))
        items.add(FirstAidItem(R.drawable.ic_heat_stroke, getString(R.string.str_heat_stroke)))
        return items
    }

    /*
    * This interface is created for communication with HomeFragment when back pressed
    * */
    interface HomeListener {
        fun scrollToHome()
    }
}