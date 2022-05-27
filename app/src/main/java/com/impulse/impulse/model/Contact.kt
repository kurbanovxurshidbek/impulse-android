package com.impulse.impulse.model

data class Contact(
    var photo: String,
    var name: String,
    var relation: String,
    var isExpandable: Boolean = false,
    var nestedList: ArrayList<ContactNestedItem>
)
