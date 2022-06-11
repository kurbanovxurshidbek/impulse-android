package com.impulse.impulse.fragment

import android.app.Activity
import android.content.Intent
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
}