package com.impulse.impulse.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.ContactsItemAdapter
import com.impulse.impulse.database.AppDatabase
import com.impulse.impulse.databinding.FragmentContactsBinding
import com.impulse.impulse.model.Contact
import com.impulse.impulse.model.ContactNestedItem
import com.impulse.impulse.utils.SpacesItemDecoration

class ContactsFragment : BaseFragment() {

    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        val view = binding.root
        appDatabase = AppDatabase.getInstance(requireContext())
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
        nestedItems.add(ContactNestedItem("+998901203022", "Help"))
        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653587106660-4908e9a7bae7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60",
                "John", "Son", false, nestedItems
            )
        )

        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653587106660-4908e9a7bae7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60",
                "John", "Son", false, nestedItems
            )
        )

        items.add(
            Contact(
                "https://images.unsplash.com/photo-1653587106660-4908e9a7bae7?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwyfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=500&q=60",
                "John", "Son", false, nestedItems
            )
        )
        return items
    }
}