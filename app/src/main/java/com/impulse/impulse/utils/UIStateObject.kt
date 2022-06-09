package com.impulse.impulse.utils

sealed class UIStateObject<out T> {
    data class SUCCESS<out T>(val data: T) : UIStateObject<T>()
    data class ERROR(val message: String) : UIStateObject<Nothing>()
    object LOADING : UIStateObject<Nothing>()
    object EMPTY : UIStateObject<Nothing>()
}
