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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulse.impulse.R
import com.impulse.impulse.adapter.ContactItemAdapter
import com.impulse.impulse.data.local.AppDatabase
import com.impulse.impulse.data.local.entity.Contact
import com.impulse.impulse.databinding.DialogContactMessageViewBinding
import com.impulse.impulse.databinding.DialogDeleteMessageBinding
import com.impulse.impulse.databinding.FragmentContactsBinding
import com.impulse.impulse.utils.SpacesItemDecoration


class ContactsFragment : BaseFragment() {
    private var _binding: FragmentContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val REQUEST_READ_CONTACTS_PERMISSION = 0
    private val REQUEST_CONTACT = 1

    private lateinit var contactAdapter: ContactItemAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var contacts: ArrayList<Contact>

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
                            val contact = Contact(name, contactNumber)
                            saveContactToDatabase(contact)
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

    private fun saveContactToDatabase(contact: Contact) {
        appDatabase.contactDao().addContact(contact)
        contacts.add(contact)
        contactAdapter.notifyItemInserted(contacts.size - 1)
        refreshAdapter(contacts)
    }


    private fun initViews() {
        // Intent to pick contacts
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

        contacts = ArrayList()
        appDatabase = AppDatabase.getInstance(requireActivity())
        contacts = appDatabase.contactDao().getAllContacts() as ArrayList<Contact>

        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(context)
            val decoration = SpacesItemDecoration(20)
            recyclerView.addItemDecoration(decoration)

            refreshAdapter(contacts)

            fab.setOnClickListener {
                startActivityForResult(pickContact, REQUEST_CONTACT)
            }

            ivEdit.setOnClickListener {
                setEditDialog()
            }
        }

        requestContactsPermission();
    }

    private fun setEditDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val dialogBinding = DialogContactMessageViewBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        dialogBinding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun refreshAdapter(contacts: ArrayList<Contact>) {
        contactAdapter = ContactItemAdapter(this@ContactsFragment, contacts)
        binding.recyclerView.adapter = contactAdapter
    }

    @Deprecated("Deprecated in Java")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            refreshAdapter(contacts)
        }
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

    fun deleteContactFromDatabase(contact: Contact, position: Int) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        val dialogBinding = DialogDeleteMessageBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()

        dialogBinding.btnOk.setOnClickListener {
            appDatabase.contactDao().deleteContact(contact)
            contacts.remove(contact)
            contactAdapter.notifyItemRemoved(position)
            refreshAdapter(contacts)
            dialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}