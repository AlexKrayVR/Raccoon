package alex.com.raccoon.ui.fragments

import alex.com.raccoon.R
import alex.com.raccoon.common.logDebug
import alex.com.raccoon.databinding.FragmentMainBinding
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainFragment : BaseLocationFragment() {
    private var binding: FragmentMainBinding? = null

    override fun locationResult(location: Location) {
        logDebug("locationResult: ${location.toString()}")
    }

    override fun hideLoadingLocation() {
    }

    override fun performIfNoLocationPermission() {
        hideLoadingLocation()
    }

    override fun showLocationProviderDisabledSnackbar() {
        val snackbar: Snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.root),
            R.string.noGPS,
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(R.string.turnOnGPS) { view: View? ->
            resultEnableLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackbar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        removeLocationUpdates()
        super.onDestroyView()
    }
}
