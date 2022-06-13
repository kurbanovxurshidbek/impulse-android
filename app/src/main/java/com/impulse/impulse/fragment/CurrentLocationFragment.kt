package com.impulse.impulse.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.impulse.impulse.R
import com.impulse.impulse.databinding.FragmentCurrentLocationBinding
import com.impulse.impulse.utils.InternetPermission
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 *Ambulance tracking with google map**
 **/

class CurrentLocationFragment : Fragment(), OnMapReadyCallback {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private val TAG: String = "MAIN_ACTIVITY"
    private var driverMarker: Marker? = null
    private var _binding: FragmentCurrentLocationBinding? = null
    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val REQUEST_CODE = 101
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCurrentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        initViews()
        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap
            listenLocationOfDriver()
        }
        return view
    }


    private fun initViews() {
        /** Check permission Internet **/
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (InternetPermission.isInternetAvailable(requireContext().applicationContext)) {
            fetchLocation()
        } else {
            Toast.makeText(requireContext(), "Internet ulanmagan", Toast.LENGTH_SHORT).show()
        }

        mapFragment =
            childFragmentManager.findFragmentById(com.impulse.impulse.R.id.map_user) as SupportMapFragment
    }


    /** ambulance coming condition **/
    private fun listenLocationOfDriver() {

        val dbRef = FirebaseDatabase.getInstance().reference

        /** this is where the coordinates are exchanged from the databases **/

        dbRef.child("driver_points").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: $snapshot")

                val value = snapshot.value as HashMap<*, *>

                val latitude = (value["latitude"] ?: 0.0).toString().toDouble()
                val longitude = (value["longitude"] ?: 0.0).toString().toDouble()
                val status = (value["status"] ?: "").toString()
                val latLng = LatLng(latitude, longitude)
                if (status == "success") {
                    googleMap?.let { gMap ->
                        if (driverMarker == null) {
                            driverMarker = gMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .anchor(0.5f, 0.5f)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_circle))
                                    .flat(true)
                            )
                        } else {
                            driverMarker?.position?.let { oldPosition ->
                                val bearing = bearingBetweenLocations(oldPosition, latLng)
                                rotateMarker(driverMarker!!, bearing.toFloat())
                                animateMarker(gMap, driverMarker!!, latLng, false)
                            }

                        }

                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
                    }
                } else {
                    fetchLocation()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    /** without a call to see the location **/

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                mapFragment!!.getMapAsync(this)
            }
        }
    }

    /** to use the allowed map **/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /** map ready mode **/
    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        googleMap.addMarker(markerOptions)
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