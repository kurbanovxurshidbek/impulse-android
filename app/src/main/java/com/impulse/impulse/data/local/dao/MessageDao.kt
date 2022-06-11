package com.impulse.impulse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.impulse.impulse.data.local.entity.Message

@Dao
interface MessageDao {

    @Insert
    fun addMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Query("SELECT * FROM Message")
    fun getMessage(): List<Message>
}