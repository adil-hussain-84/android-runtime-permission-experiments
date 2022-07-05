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
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { updateTextView() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.shouldShowLocationPermissionRationaleButton).setOnClickListener { shouldShowLocationPermissionRationale() }
        findViewById<View>(R.id.requestLocationPermissionButton).setOnClickListener { requestLocationPermission() }
    }

    override fun onStart() {
        super.onStart()
        updateTextView()
    }

    private fun shouldShowLocationPermissionRationale() {
        val message = if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            "Yes"
        } else {
            "No"
        }
        showToast(message)
    }

    private fun requestLocationPermission() {
        if (isLocationPermissionGranted()) {
            showToast("Nothing to do. App already has location permission.")
            return
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            val message = getString(R.string.location_permission_rationale)

            val negativeButtonText = getString(R.string.deny)
            val positiveButtonText = getString(R.string.allow)

            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(message)
                .setNegativeButton(negativeButtonText, null)
                .setPositiveButton(positiveButtonText) { _, _ -> askForGeolocationPermission() }
                .show()
        } else {
            askForGeolocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForGeolocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        locationPermissionRequest.launch(permissions)
    }

    private fun updateTextView() {
        findViewById<TextView>(R.id.textView).text = if (isLocationPermissionGranted()) {
            "The app has location permission."
        } else {
            "The app does not have location permission."
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
