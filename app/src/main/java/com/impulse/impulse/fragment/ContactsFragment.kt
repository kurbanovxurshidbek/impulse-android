package com.impulse.impulse.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.adapter.ContactItemAdapter
import com.impulse.impulse.database.AppDatabase
import com.impulse.impulse.database.model.Contact
import com.impulse.impulse.databinding.FragmentContactsBinding
import com.impulse.impulse.utils.SpacesItemDecoration
import com.impulse.impulse.viewmodel.ContactViewModel


class ContactsFragment : BaseFragment() {
    private lateinit var contactViewModel: ContactViewModel
    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val REQUEST_READ_CONTACTS_PERMISSION = 0
    private val REQUEST_CONTACT = 1

    private lateinit var contactAdapter: ContactItemAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var contacts: List<Contact>

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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION && grantResults.isNotEmpty()) {
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri: Uri? = data.data

            // Perform your query - the contactUri
            // is like a "where" clause here
            val cursor: Cursor? = activity?.contentResolver!!
                .query(contactUri!!, null, null, null, null)
            val cursorPhone: Cursor?
            try {
                if (cursor!!.moveToFirst()) {
                    // get contact details
                    val contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val contactThumbnail =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val name: String =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val idResults =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val idResultHold = idResults.toInt()

                    if (idResultHold == 1) {
                        cursorPhone = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null
                        )
                        //a contact may have multiple phone numbers
                        while (cursorPhone!!.moveToNext()) {
                            //get phone number
                            val contactNumber =
                                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            //set phone number
                            appDatabase.contactDao().addContact(Contact(name, contactNumber))
                        }
                        cursorPhone.close()
                    }

                }

                // Double-check that you
                // actually got results
                if (cursor.count == 0) return

            } finally {
                cursor?.close()
            }
        }
    }


    private fun initViews() {
        contactViewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        // Intent to pick contacts
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

        appDatabase = AppDatabase.getInstance(requireActivity())
        contacts = ArrayList()
        contacts = appDatabase.contactDao().getAllContacts()
        Log.d("@@@", "initViews: ${contacts.size}")
        contactAdapter = ContactItemAdapter(
            contacts,
            object : ContactItemAdapter.OnItemClickListener {
                override fun onItemClicked(
                    layout: LinearLayout,
                    position: Int,
                    isExpandable: Boolean
                ) {
                    layout.isVisible = isExpandable
                    contactAdapter.notifyItemChanged(position)
                }
            })

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(context)
            val decoration = SpacesItemDecoration(20)
            recyclerView.addItemDecoration(decoration)

            recyclerView.adapter = contactAdapter

            llAdd.setOnClickListener {
                startActivityForResult(pickContact, REQUEST_CONTACT)
            }
        }

        requestContactsPermission();
    }

    private fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) ==
                PackageManager.PERMISSION_GRANTED
    }

    // Request contact permission if it
    // has not been granted already
    private fun requestContactsPermission() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS_PERMISSION
            )
        }
    }
}