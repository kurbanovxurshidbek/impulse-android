package uz.impulse.impulse.utils

import android.util.Log
import uz.impulse.impulse.data.remote.PhoneNumberHttp.IS_TESTER

object Logger {
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