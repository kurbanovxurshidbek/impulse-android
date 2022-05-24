package com.impulse.impulse.manager

import android.content.Context
import android.content.SharedPreferences

class PrefsManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences?

    companion object {
        private var prefsManager: PrefsManager? = null

        fun getInstance(context: Context): PrefsManager? {
            if (prefsManager == null) {
                prefsManager = PrefsManager(context)
            }
            return prefsManager
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
    }

    // check first time user or other
    fun setFirstTime(key: String?, value: Boolean?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putBoolean(key, value!!)
        prefsEditor.apply()
    }

    fun isFirstTime(key: String?): Boolean {
        return sharedPreferences?.getBoolean(key, true) ?: false
    }

    // check logged in or not
    fun setLoggedIn(key: String?, value: Boolean?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putBoolean(key, value!!)
        prefsEditor.apply()
    }

    fun isLoggedIn(key: String?): Boolean {
        return sharedPreferences?.getBoolean(key, true) ?: false
    }

    fun saveData(key: String?, value: String?) {
        val prefsEditor = sharedPreferences!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun getData(key: String?): String? {
        return if (sharedPreferences != null) sharedPreferences.getString(key, "") else "en"
    }

    fun deleteData(key: String?) {
        val editor = sharedPreferences!!.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clearAll() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }
}