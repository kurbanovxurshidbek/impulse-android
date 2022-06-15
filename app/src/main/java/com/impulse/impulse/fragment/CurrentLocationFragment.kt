package com.impulse.impulse.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.impulse.impulse.R
import com.impulse.impulse.databinding.FragmentCurrentLocationBinding
import com.impulse.impulse.manager.PrefsManager
import com.impulse.impulse.utils.Connections
import com.impulse.impulse.utils.Constants
import com.impulse.impulse.utils.InternetPermission
import com.impulse.impulse.utils.PermissionGPS
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


/**
 *Ambulance tracking with google map**
 **/

class CurrentLocationFragment : Fragment(),GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var enabled: Boolean? = null
    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private val TAG: String = "MAIN_ACTIVITY"
    private var driverMarker: Marker? = null
    private var driverMarkerSecond: Marker? = null
    private var _binding: FragmentCurrentLocationBinding? = null
    var currentLocation : Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val REQUEST_CODE = 101
    private val binding get() = _binding!!
    var track = false
    private var locationRequest: LocationRequest? = null
    private var resultReceiver: ResultReceiver? = null
    //////////
    var latJust = LatLng(41.326556, 69.228184)
    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val UPDATE_INTERVAL: Long = 500
        private const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 5
    }

    override fun onStart() {
        super.onStart()
        if (Connections.checkConnection(requireContext())) {
            PermissionGPS(requireActivity())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        _binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap
//            fetchLocation()
//            listenLocationOfDriver()
            if (InternetPermission.isInternetAvailable(requireContext().applicationContext)) {
                fetchLocation()
                listenLocationOfDriver()
            } else {
                Toast.makeText(requireContext(),"Internet ulanmagan",Toast.LENGTH_LONG).show()
            }
        }
        return view
    }


    private fun initViews() {
        var count = 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }


        /** Check permission Internet **/
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        resultReceiver = object : ResultReceiver(Handler()) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            }
        }
        binding.apply {
            fbgpss.setOnClickListener {
                if (InternetPermission.isInternetAvailable(requireContext().applicationContext)) {
                    fetchLocation()
                } else {
                    if (count < 3) {
                        Toast.makeText(requireContext(),"Internet ulanmagan",Toast.LENGTH_LONG).show()
                    }
                    count++
                }
                if (Connections.checkConnection(requireContext())) {
                    PermissionGPS(requireActivity())
                }
                track = true
                fetchLocation()
            }

            fblocations.setOnClickListener {
                track = false
                listenLocationOfDriver()
            }

            fbsatelits.setOnClickListener {
                if (googleMap != null) {
                    val MapType = googleMap!!.mapType
                    if (MapType == 1) {
                        googleMap!!.mapType = GoogleMap.MAP_TYPE_HYBRID
                    } else{
                        googleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                    }
                }
            }

            locationRequest = LocationRequest()
            locationRequest!!.interval = UPDATE_INTERVAL
            locationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL
            locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            mapFragment = childFragmentManager.findFragmentById(com.impulse.impulse.R.id.relative) as SupportMapFragment

        }
    }

    override fun onMapClick(point: LatLng) {
        driverMarkerSecond = null
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker == driverMarkerSecond) {
            driverMarkerSecond = null
            return true
        }

        driverMarkerSecond = marker
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.") // grantResults.length > 0
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> fetchLocation()
                else ->  { requestPermission() }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }



    /** ambulance coming condition **/
    private fun listenLocationOfDriver() {

        /** this is where the coordinates are exchanged from the databases **/

        if (track == false) {
            val dbRef = FirebaseDatabase.getInstance().reference
            dbRef.child("driver_points").addValueEventListener(object : ValueEventListener {
                @SuppressLint("MissingPermission")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: $snapshot")

                    val value = snapshot.value as HashMap<*, *>

                    val latitude = (value["latitude"] ?: 0.0).toString().toDouble()
                    val longitude = (value["longitude"] ?: 0.0).toString().toDouble()
                    val status = (value["status"] ?: "").toString()
                    if (status == "success") {
                        val latLng = LatLng(latitude, longitude)
                        googleMap?.let { gMap ->
                            if (driverMarker == null) {
                                driverMarker = gMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance))
                                        .anchor(0.5f, 0.5f)
                                        .flat(true)

                                )
                            } else {
                                val bearing = bearingBetweenLocations(latJust, latLng)
                                rotateMarker(driverMarker!!, bearing.toFloat())
                                animateMarker(gMap, driverMarker!!, latLng, false)
                                driverMarker?.position?.let { oldPosition ->

                                }

                            }

                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                        }
                    }else {
                        if (driverMarker!= null) {
                            driverMarker?.remove()
                            driverMarker = null

                        }else {
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    /** without a call to see the location **/

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
                mapFragment!!.getMapAsync(this)

            }
        }
    }

    /** map ready mode **/
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.setOnMarkerClickListener(this)
        setupMap()
        Log.d("Tayyor","Tayyor")
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        googleMap?.isMyLocationEnabled = true
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLong = LatLng(location.latitude,location.longitude)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,16f))
            }
        }
    }

    /** two distance detection **/
fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
    val lat1 = latLng1.latitude
    val lng1 = latLng1.longitude
    val lat2 = latLng2.latitude
    val lng2 = latLng2.longitude
    val fLat: Double = degreeToRadians(lat1)
    val fLong: Double = degreeToRadians(lng1)
    val tLat: Double = degreeToRadians(lat2)
    val tLong: Double = degreeToRadians(lng2)
    val dLon = tLong - fLong
    val degree: Double = radiansToDegree(
        atan2(
            sin(dLon) * cos(tLat),
            cos(fLat) * sin(tLat) - sin(fLat) * cos(tLat) * cos(dLon)
        )
    )
    return if (degree >= 0) {
        degree
    } else {
        360 + degree
    }
}

/** to move the icon in the address **/
private fun degreeToRadians(latLong: Double): Double {
    return Math.PI * latLong / 180.0
}

private fun radiansToDegree(latLong: Double): Double {
    return latLong * 180.0 / Math.PI
}

internal var isMarkerRotating = false
fun rotateMarker(marker: Marker, toRotation: Float) {
    if (!isMarkerRotating) {
        val handler = Handler(Looper.getMainLooper())
        val start = SystemClock.uptimeMillis()
        val startRotation = marker.rotation
        val duration: Long = 1000
        val interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                isMarkerRotating = true
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val rot = t * toRotation + (1 - t) * startRotation
                marker.rotation = if (-rot > 180) rot / 2 else rot
                if (t < 1.0) {
                    handler.postDelayed(this, 26)
                } else {
                    isMarkerRotating = false
                }
            }
        })
    }
}

internal var isDriverMarkerMoving = false
fun animateMarker(
    googleMap: GoogleMap,
    driverMarker: Marker,
    toPosition: LatLng,
    hideMarker: Boolean,
) {
    if (!isDriverMarkerMoving) {
        val start = SystemClock.uptimeMillis()
        val proj = googleMap.projection
        val startPoint = proj.toScreenLocation(driverMarker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 2000

        val interpolator = LinearInterpolator()

        val driverMarkerHandler = Handler(Looper.getMainLooper())
        driverMarkerHandler.post(object : Runnable {
            override fun run() {
                isDriverMarkerMoving = true
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                driverMarker.position = LatLng(lat, lng)

                if (t < 1.0) {
                    driverMarkerHandler.postDelayed(this, 16)
                } else {
                    driverMarker.isVisible = !hideMarker
                    isDriverMarkerMoving = false
                }
            }
        })
    }
}

    override fun onConnected(p0: Bundle?) {
        if (!enabled!!) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            fetchLocation()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}
