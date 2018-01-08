package upm.dam.voteitup.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import upm.dam.voteitup.ApiClient
import upm.dam.voteitup.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnSuccessListener
import upm.dam.voteitup.entities.Area
import android.location.Geocoder
import android.location.Location
import android.support.v4.app.ActivityCompat
import java.util.*
import android.support.v4.content.ContextCompat
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import upm.dam.voteitup.random
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.HashMap


class NearByActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private lateinit var locationManager : LocationManager
    private var mRequestingLocationUpdates : Boolean = false
    private var ACCESS_COARSE_LOCATION_RESULT: Int = 0
    private var mLocationRequest: LocationRequest? = null
    private var isLocationAllowed: Boolean = false
    private lateinit var mHashMap :HashMap<Marker, Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_by)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        isLocationAllowed = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Show an explanation to the user *asynchronously*
        } else ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),ACCESS_COARSE_LOCATION_RESULT)

        if(isLocationAllowed){
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.getLocations()) {
                        mMap.moveCamera(CameraUpdateFactory.
                                newLatLng(LatLng(location.latitude,location.longitude)))

                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_COARSE_LOCATION_RESULT -> {
                // If request is cancelled, the result arrays are empty.
                isLocationAllowed = grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                return
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        launch(UI) {
            val getPollsAsync = async { ApiClient.getPolls() }
            val polls = getPollsAsync.await() ?: error("Error retrieving Poll info")
            mHashMap= HashMap()
            polls.filter{it.Area != null}.forEach {
                var coordinatesCityCountry = getCoordinatesFrom(it.Area!!)
                if(coordinatesCityCountry != null) {
                        val marker = mMap.addMarker(MarkerOptions()
                                .position(LatLng(coordinatesCityCountry.second!!,
                                        coordinatesCityCountry.first!!))
                                .title(it.text))
                        mHashMap.put(marker, it.id!!.toInt())
                    }

                }


        }
        mMap.setOnInfoWindowClickListener({
            marker ->
            val pos = mHashMap[marker]
            val intent = Intent(this.applicationContext, PollsActivity::class.java)
            intent.putExtra(PollsActivity.INTENT_POLL_ID, pos.toString())
            startActivity(intent)
        })

        if (isLocationAllowed){

            createLocationRequest()
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mMap.moveCamera(CameraUpdateFactory.
                            newLatLngZoom(LatLng(location.latitude,location.longitude), 10.0F))

                }
            }
        }else
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCoordinatesFrom(area:Area): Pair<Double?, Double?>? {

        val gcd = Geocoder(this.baseContext, Locale.getDefault())
        ///the best would be add the size of the +-(city or country)/2, but for now are random!
        val offsetLo = (-0.02..0.02).random()
        val offsetLa = (-0.02..0.02).random()

        if(area.city?.isNotBlank()!!){
            val location= gcd.getFromLocationName(area.city,1).firstOrNull()
            if (location != null)
                return Pair(location?.longitude!!.plus(offsetLo),location?.latitude.plus(offsetLa))
        }

        else if(area.country?.isNotBlank()!!) {
            val location= gcd.getFromLocationName(area.country, 1).firstOrNull()
            if (location != null)
                return Pair(location?.longitude!!.plus(offsetLo),location?.latitude!!.plus(offsetLa))
        }
        return null
    }

    protected fun createLocationRequest() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (isLocationAllowed)
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, null /* Looper */)
    }

}





