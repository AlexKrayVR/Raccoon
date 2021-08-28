package alex.com.raccoon.common

import android.Manifest
import com.google.android.gms.location.LocationRequest


//if location disabled
const val LOCATION_ENABLE_CODE = 10

//location permissions
const val LOCATION_PERMISSION_REQUEST_CODE = 20
val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

//location request
const val LOCATION_REQUEST_INTERVAL:Long = 100
const val LOCATION_REQUEST_FASTEST_INTERVAL:Long = 100
fun getLocationRequest(): LocationRequest {
    val locationRequest = LocationRequest.create()
    locationRequest.interval = LOCATION_REQUEST_INTERVAL
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
    return locationRequest
}