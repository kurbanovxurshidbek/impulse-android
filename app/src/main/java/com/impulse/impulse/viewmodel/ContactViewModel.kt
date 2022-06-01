package com.impulse.impulse.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.impulse.impulse.database.AppDatabase
import com.impulse.impulse.database.model.Contact

class ContactViewModel : ViewModel() {
    val allContacts = MutableLiveData<ArrayList<Contact>>()

    fun saveContact(context: Context, contact: Contact) {
        AppDatabase.getInstance(context).contactDao().addContact(contact)
    }
}