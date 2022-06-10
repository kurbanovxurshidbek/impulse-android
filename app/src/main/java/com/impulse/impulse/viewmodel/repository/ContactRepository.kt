package com.impulse.impulse.viewmodel.repository

import com.impulse.impulse.data.local.dao.ContactDao
import com.impulse.impulse.data.local.entity.Contact

class ContactRepository(private val contactDao: ContactDao) {

    fun addContact(contact: Contact) = contactDao.addContact(contact)
//    suspend fun getAllContacts() = contactDao.getAllContacts()
    fun deleteContact(contact: Contact) = contactDao.deleteContact(contact)


}