package com.tazkiyatech.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { updateTextViews() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.requestLocationPermissionButton).setOnClickListener { onRequestLocationPermissionButtonClicked() }
    }

    override fun onStart() {
        super.onStart()
        updateTextViews()
    }

    private fun onRequestLocationPermissionButtonClicked() {
        if (isLocationPermissionGranted()) {
            showToast("Nothing to do. App already has location permission.")
            return
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            val message = getString(R.string.location_permission_rationale)
            val positiveButtonText = getString(android.R.string.ok)

            AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ -> requestLocationPermission() }
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

    private fun requestLocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        locationPermissionRequest.launch(permissions)
    }

    @Suppress("SameParameterValue")
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
