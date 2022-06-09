package com.impulse.impulse.utils

sealed class UIStateList<out T> {
    data class SUCCESS<out T>(val data: List<T>) : UIStateList<T>()
    data class ERROR(val message: String) : UIStateList<Nothing>()
    object LOADING : UIStateList<Nothing>()
    object EMPTY : UIStateList<Nothing>()
}
