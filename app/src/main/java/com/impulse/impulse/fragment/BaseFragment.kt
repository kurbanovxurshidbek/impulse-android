package com.impulse.impulse.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.impulse.impulse.R
import com.impulse.impulse.activity.MainActivity
import com.impulse.impulse.activity.SignUpActivity

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
}