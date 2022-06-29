package uz.impulse.impulse.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.impulse.impulse.R
import uz.impulse.impulse.activity.MainActivity
import uz.impulse.impulse.activity.SignUpActivity

/*
*  BaseFragment is parent for all Fragments
* */
open class BaseFragment : Fragment() {

    fun callSignUpActivity(activity: Activity) {
        val intent = Intent(context, SignUpActivity::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun callMainActivity(activity: Activity) {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity.finish()
    }

    fun navigateToProfileFragment() {
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId =
            R.id.profileFragment
    }

    fun navigateToFirstAidFragment() {
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId =
            R.id.firstAidFragment
    }

    fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun vibrate() {
        val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
}