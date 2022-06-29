package uz.impulse.impulse.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.impulse.impulse.data.local.entity.Message

@Dao
interface MessageDao {

    @Insert
    fun addMessage(message: Message)

    @Query("DELETE FROM  Message")
    fun deleteMessage()

    @Query("SELECT message_text FROM Message")
    fun getMessage(): String
}