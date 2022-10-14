package com.tazkiyatech.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            map.forEach { mapEntry ->
                if (mapEntry.value) {
                    // given that the permission has been granted, clear the heuristic from persistent storage
                    persistentStorage.userHasAcknowledgedLocationPermissionRationale = false
                }
            }
            updateTextViews()
        }

    private val persistentStorage = PersistentStorage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.requestLocationPermissionButton).setOnClickListener { onRequestLocationPermissionButtonClicked() }
    }

    override fun onStart() {
        super.onStart()

        if (isLocationPermissionGranted()) {
            // given that the permission has been granted, clear the heuristic from persistent storage
            persistentStorage.userHasAcknowledgedLocationPermissionRationale = false
        }

        updateTextViews()
    }

    private fun onRequestLocationPermissionButtonClicked() {
        if (isLocationPermissionGranted()) {
            showToast("Nothing to do. App already has location permission.")
            return
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.location_permission_rationale_1))
                .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    persistentStorage.userHasAcknowledgedLocationPermissionRationale = true
                    requestLocationPermission()
                }
                .show()
        } else if (userHasPreviouslyAcknowledgedLocationPermissionRationale()) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.location_permission_rationale_2))
                .setNegativeButton("No, I'm good", null)
                .setPositiveButton("Ok, sure") { _, _ -> showApplicationDetailsSettingsScreen() }
                .show()
        } else {
            requestLocationPermission()
        }
    }

    private fun updateTextViews() {
        findViewById<TextView>(R.id.isLocationPermissionGrantedTextView).text =
            if (isLocationPermissionGranted()) {
                "Is location permission granted? Yes"
            } else {
                "Is location permission granted? No"
            }

        findViewById<TextView>(R.id.shouldShowRequestPermissionRationaleTextView).text =
            if (shouldShowLocationPermissionRationale()) {
                "Should show request permission rationale? Yes"
            } else {
                "Should show request permission rationale? No"
            }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowLocationPermissionRationale(): Boolean {
        return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun userHasPreviouslyAcknowledgedLocationPermissionRationale(): Boolean {
        return persistentStorage.userHasAcknowledgedLocationPermissionRationale
    }

    private fun requestLocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        locationPermissionRequest.launch(permissions)
    }

    private fun showApplicationDetailsSettingsScreen() {
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
        )
    }

    @Suppress("SameParameterValue")
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
