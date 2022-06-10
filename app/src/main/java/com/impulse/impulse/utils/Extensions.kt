package com.impulse.impulse.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

object Extensions {
    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}