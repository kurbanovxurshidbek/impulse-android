package com.impulse.impulse.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.impulse.impulse.model.Contact

@Dao
interface ContactDao {

    @Insert
    fun insertContact(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getAllContact(): ArrayList<Contact>

    @Query("SELECT * FROM Contact WHERE contactId=:contactId")
    fun getItem(contactId: Int): Contact

    @Query("DELETE FROM Contact")
    fun clearContact()

    @Query("DELETE FROM Contact WHERE contactId=:contactId")
    fun removeContact(contactId: Int)
}