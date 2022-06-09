package com.impulse.impulse.utils

import android.util.Log

object Logger {
    const val IS_TESTER = true

    fun d(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (IS_TESTER) Log.i(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (IS_TESTER) Log.v(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (IS_TESTER) Log.e(tag, msg)
    }
}