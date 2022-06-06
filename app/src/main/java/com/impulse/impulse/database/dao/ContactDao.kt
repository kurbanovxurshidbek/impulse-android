package com.impulse.impulse.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.impulse.impulse.database.model.Contact

@Dao
interface ContactDao {

    @Insert
    fun addContact(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): List<Contact>

    @Delete
    fun deleteContact(contact: Contact)
}