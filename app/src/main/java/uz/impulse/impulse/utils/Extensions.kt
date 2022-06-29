package uz.impulse.impulse.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

object Extensions {
    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.isValidPhoneNumber(string: String): Boolean {
        //+998901203022
        if (string.isBlank()) return true
        val prefix = arrayListOf(
            "90", "91", "93", "94", "95", "97", "99", "88",
            "33"
        )
        if (prefix.contains(string.substring(4, 5)))
            return true
        return false
    }
}