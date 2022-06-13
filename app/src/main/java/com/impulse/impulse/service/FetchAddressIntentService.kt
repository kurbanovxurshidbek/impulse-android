package com.impulse.impulse.service

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.text.TextUtils
import android.util.Log
import com.impulse.impulse.R
import com.impulse.impulse.utils.Constants
import java.io.IOException
import java.util.*

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
/**
 * This constructor is required, and calls the super IntentService(String)
 * constructor with the name for a worker thread.
 */
class FetchAddressIntentService : IntentService(TAG) {

    /**
     * The receiver where results are forwarded from this service.
     */
    private var receiver: ResultReceiver? = null

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: [ResultReceiver] in * MainActivity is defined to
     * process the content sent from this service.
     *
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""

        receiver = intent!!.getParcelableExtra(Constants.RECEIVER)

        if (receiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.")
            return
        }

        val location = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided)
            Log.wtf(TAG, errorMessage)
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
            return
        }

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1)
        } catch (ioException: IOException) {
            errorMessage = getString(R.string.service_not_available)
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used)
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.latitude +
                    ", Longitude = " + location.longitude, illegalArgumentException)
        }

        if (addresses == null || addresses.isEmpty()) {
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage)
        } else {
            val address = addresses[0]
            val addressFragments = ArrayList<String>()

            for (i in 0..address.maxAddressLineIndex) {
                addressFragments.add(address.getAddressLine(i))
            }

            Log.i(TAG, getString(R.string.address_found))
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                TextUtils.join(System.getProperty("line.separator")!!, addressFragments))
        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private fun deliverResultToReceiver(resultCode: Int, message: String) {
        val bundle = Bundle()
        bundle.putString(Constants.RESULT_DATA_KEY, message)
        receiver!!.send(resultCode, bundle)
    }

    companion object {
        private const val TAG = "FetchAddressIS"
    }
}