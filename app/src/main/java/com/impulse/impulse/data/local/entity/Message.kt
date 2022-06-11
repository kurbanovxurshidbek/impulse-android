package com.impulse.impulse.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Message")
class Message {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "message_text")
    var message: String? = null

    constructor(message: String?) {
        this.message = message
    }

    constructor()
}