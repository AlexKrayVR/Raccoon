package alex.com.raccoon.ui.fragments

import alex.com.raccoon.R
import alex.com.raccoon.common.LOCATION_PERMISSIONS
import alex.com.raccoon.common.LOCATION_PERMISSION_REQUEST_CODE
import alex.com.raccoon.common.getLocationRequest
import alex.com.raccoon.common.logDebug
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

abstract class BaseLocationFragment : Fragment() {


    var resultEnableLocation = registerForActivityResult(
        ActivityResultContracts
            .StartActivityForResult()
    ) { result ->
        logDebug("resultEnableLocation")
        checkIfLocationProviderEnabled()
    }

    protected abstract fun locationResult(location: Location)
    protected abstract fun hideLoadingLocation()
    protected abstract fun performIfNoLocationPermission()
    protected abstract fun showLocationProviderDisabledSnackbar()

    protected fun getLocationPermission() {
        if (hasLocationPermission()) {
            logDebug("Method getLocationPermission() - Location permission granted")
            checkIfLocationProviderEnabled()
        } else {
            logDebug("Method getLocationPermission() - Location permission not granted")
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            LOCATION_PERMISSIONS,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    //handle the result from requested permissions.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (hasLocationPermission()) {
                logDebug("Location Permissions Result: Success!")
                checkIfLocationProviderEnabled()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0])) {
                dialogExplanationRequestLocationPermission(getString(R.string.explanationRequestLocationPermission))
            } else {
                logDebug("Location Permissions Result: Failed!")
                performIfNoLocationPermission()
            }
        } else {
            super.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        }
    }

    private fun dialogExplanationRequestLocationPermission(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setTitle(getString(R.string.attention))
            .setOnCancelListener { dialogInterface: DialogInterface? ->
                requestPermissions()
            }
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialogInterface: DialogInterface?, i: Int ->
                requestPermissions()
            }
            .create()
            .show()
    }

    //we don't need to check location permission again,
    //because the method is only called if permission received
    @SuppressLint("MissingPermission")
    private fun getUserCurrentLocation() {
        activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
                .requestLocationUpdates(getLocationRequest(), locationCallback, Looper.getMainLooper())
        }

    }

    protected fun removeLocationUpdates() {
        activity?.let {
            LocationServices
                .getFusedLocationProviderClient(it)
            .removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            removeLocationUpdates()
            hideLoadingLocation()
            locationResult(locationResult.lastLocation)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                requireContext(),
                LOCATION_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkIfLocationProviderEnabled() {
        logDebug("checkIfLocationProviderEnabled")
        if (isLocationProviderEnabled()) {
            getUserCurrentLocation()
        } else {
            showLocationProviderDisabledSnackbar()
        }
    }

    private fun isLocationProviderEnabled(): Boolean {
        val manager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}