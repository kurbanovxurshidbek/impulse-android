package com.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.ContactsItemAdapter
import com.impulse.impulse.databinding.FragmentContactsBinding
import com.impulse.impulse.model.Contact
import com.impulse.impulse.model.ContactNestedItem
import com.impulse.impulse.utils.SpacesItemDecoration

class ContactsFragment : BaseFragment() {

    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
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
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(context)
            val decoration = SpacesItemDecoration(20)
            recyclerView.addItemDecoration(decoration)
            recyclerView.adapter = ContactsItemAdapter(requireContext(), getAllContacts())

        }
    }

    private fun getAllContacts(): ArrayList<Contact> {
        val items = ArrayList<Contact>()
        val nestedItems = ArrayList<ContactNestedItem>()
        nestedItems.add(ContactNestedItem("+998901203022", "Yordaaaam"))
        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653257924069-dac9af08fdf6?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyNXx8fGVufDB8fHx8&auto=format&fit=crop&w=500",
                "Appolonia", "wife", false, nestedItems

            )
        )

        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653257924069-dac9af08fdf6?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyNXx8fGVufDB8fHx8&auto=format&fit=crop&w=500",
                "Appolonia", "wife", false, nestedItems

            )
        )

        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653257924069-dac9af08fdf6?crop=entropy&cs=tinysrgb&fm=jpg&ixlib=rb-1.2.1&q=60&raw_url=true&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyNXx8fGVufDB8fHx8&auto=format&fit=crop&w=500",
                "Appolonia", "wife", false, nestedItems

            )
        )

        return items
    }
}