package com.impulse.impulse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.impulse.impulse.data.local.entity.Contact

@Dao
interface ContactDao {

    @Insert
    fun addContact(contact: Contact)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): List<Contact>

//    @Delete
//    suspend fun deleteContact(contact: Contact)
}