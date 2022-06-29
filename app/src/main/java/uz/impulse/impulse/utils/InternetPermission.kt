package uz.impulse.impulse.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager

class InternetPermission {
    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            return infoMobile!!.isConnected || infoWifi!!.isConnected
        }
    }
}