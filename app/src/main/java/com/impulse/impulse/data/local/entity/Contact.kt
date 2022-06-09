package com.impulse.impulse.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Contact")
class Contact {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "contact_name")
    var name: String? = null

    @ColumnInfo(name = "contact_phone_number")
    var number: String? = null

    constructor(name: String?, number: String?) {
        this.name = name
        this.number = number
    }

    constructor()
}
