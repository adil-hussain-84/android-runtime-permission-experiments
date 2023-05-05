package com.tazkiyatech.app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private val notificationChannelGroups = NotificationChannelGroups(this)
    private val notificationChannels = NotificationChannels(this)
    private val notifications = Notifications(this)
    private val persistentStorage = PersistentStorage(this)

    private val notificationManagerCompat
        get() = NotificationManagerCompat.from(this)

    private val postNotificationsPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // given that the permission has been granted, clear the heuristic from persistent storage
                persistentStorage.userHasAcknowledgedPostNotificationsPermissionRationale = false

                requestPostNotificationsPermissionIfRequired() // this call is required in case the app has permission to post notifications but the notification channel group or notification channel is denied
                updateTextViews()
                updateButtons()
            }
        }

    private val postNotificationsPermissionSummaryTextView
        get() = findViewById<TextView>(R.id.postNotificationsPermissionSummaryTextView)

    private val enableBreakingNewsNotificationsButton
        get() = findViewById<View>(R.id.enableBreakingNewsNotificationsButton)

    private val postBreakingNewsNotificationButton
        get() = findViewById<View>(R.id.postBreakingNewsNotificationButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationChannelGroups.createNotificationChannelGroups()
        notificationChannels.createNotificationChannels()

        enableBreakingNewsNotificationsButton.setOnClickListener { onEnableBreakingNewsNotificationsButtonClicked() }
        postBreakingNewsNotificationButton.setOnClickListener { onPostBreakingNewsNotificationButtonClicked() }
    }

    override fun onStart() {
        super.onStart()
        updateTextViews()
        updateButtons()
    }

    private fun onEnableBreakingNewsNotificationsButtonClicked() {
        requestPostNotificationsPermissionIfRequired()
    }

    private fun onPostBreakingNewsNotificationButtonClicked() {
        notifications.showUpdateYourProgressNotification()
    }

    private fun updateTextViews() {
        postNotificationsPermissionSummaryTextView.text = if (!areNotificationsEnabled()) {
            "The app cannot post \"Breaking news\" notifications because it does not have permission to post notifications."
        } else if (!notificationChannelGroups.isNewsNotificationChannelGroupEnabled()) {
            "The app cannot post \"Breaking news\" notifications because the \"News\" notification channel group is disabled."
        } else if (!notificationChannels.isBreakingNewsNotificationChannelEnabled()) {
            "The app cannot post \"Breaking news\" notifications because the \"Breaking news\" notification channel is disabled."
        } else {
            "This app has full permission to post \"Breaking news\" notifications."
        }
    }

    private fun updateButtons() {
        enableBreakingNewsNotificationsButton.isEnabled = !isBreakingNewsNotificationChannelFullyEnabled()
        postBreakingNewsNotificationButton.isEnabled = isBreakingNewsNotificationChannelFullyEnabled()
    }

    private fun requestPostNotificationsPermissionIfRequired() {
        if (isBreakingNewsNotificationChannelFullyEnabled()) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostNotificationsPermissionOnNewerDevices()
        } else {
            requestPostNotificationsPermissionViaDeviceSettingsScreen()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPostNotificationsPermissionOnNewerDevices() {
        if (areNotificationsEnabled()) {
            // the app has permission to post notifications but the notification channel group or notification channel is denied
            requestPostNotificationsPermissionViaDeviceSettingsScreen()
        } else if (shouldShowPostNotificationsPermissionRationale()) {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.post_notifications_permission_rationale_1))
                .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    persistentStorage.userHasAcknowledgedPostNotificationsPermissionRationale = true
                    postNotificationsPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
                }.show()
        } else if (userHasPreviouslyAcknowledgedPostNotificationsPermissionRationale()) {
            requestPostNotificationsPermissionViaDeviceSettingsScreen()
        } else {
            postNotificationsPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestPostNotificationsPermissionViaDeviceSettingsScreen() {
        val message = if (!notificationManagerCompat.areNotificationsEnabled()) {
            getString(R.string.post_notifications_permission_rationale_2)
        } else if (!notificationChannelGroups.isNewsNotificationChannelGroupEnabled()) {
            getString(R.string.post_notifications_permission_rationale_3)
        } else {
            getString(R.string.post_notifications_permission_rationale_4)
        }

        AlertDialog.Builder(this).setMessage(message)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ -> showNotificationSettingsScreen() }
            .show()
    }

    private fun showNotificationSettingsScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationSettingsScreenOnNewerDevices()
        } else {
            showNotificationSettingsScreenOnOlderDevices()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotificationSettingsScreenOnNewerDevices() {
        val intent = if (!areNotificationsEnabled() || !isNewsNotificationChannelGroupEnabled()) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        } else {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannels.breakingNewsNotificationChannelId)
        }

        startActivity(intent)
    }

    private fun showNotificationSettingsScreenOnOlderDevices() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .setData(Uri.fromParts("package", packageName, null))

        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun shouldShowPostNotificationsPermissionRationale(): Boolean {
        return shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun userHasPreviouslyAcknowledgedPostNotificationsPermissionRationale(): Boolean {
        return persistentStorage.userHasAcknowledgedPostNotificationsPermissionRationale
    }

    private fun areNotificationsEnabled(): Boolean {
        return notificationManagerCompat.areNotificationsEnabled()
    }

    private fun isNewsNotificationChannelGroupEnabled(): Boolean {
        return notificationChannelGroups.isNewsNotificationChannelGroupEnabled()
    }

    private fun isBreakingNewsNotificationChannelFullyEnabled(): Boolean {
        return notificationChannels.isBreakingNewsNotificationChannelFullyEnabled()
    }
}
