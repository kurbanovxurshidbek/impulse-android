package com.impulse.impulse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "Contact")
data class Contact(
    @ColumnInfo(name = "contact_photo")
    var photo: String,
    @ColumnInfo(name = "contact_name")
    var name: String,
    @ColumnInfo(name = "contact_relation")
    var relation: String,
    @ColumnInfo(name = "contact_is_expandable")
    var isExpandable: Boolean = false,
    @ColumnInfo(name = "contact_nested_item_list")
    var nestedList: ArrayList<ContactNestedItem>,

    @PrimaryKey(autoGenerate = true)
    var contactId: Int? = null
)

data class ContactNestedItem(
    var phoneNumber: String,
    var message: String
)

class ContactsConverter {
    @TypeConverter
    fun listToJson(value: ArrayList<ContactNestedItem>?) = Gson().toJson(value)!!

    @TypeConverter
    fun jsonToList(value: String) =
        Gson().fromJson(value, Array<ContactNestedItem>::class.java).toList()
}
