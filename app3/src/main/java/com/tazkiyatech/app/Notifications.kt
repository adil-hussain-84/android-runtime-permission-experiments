package com.tazkiyatech.app

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class Notifications(private val context: Context) {

    private val notificationManager: NotificationManager
        get() = ContextCompat.getSystemService(context, NotificationManager::class.java)!!

    private val notificationChannels: NotificationChannels
        get() = NotificationChannels(context)

    /**
     * Shows the notification which reminds the user to update their progress.
     */
    fun showUpdateYourProgressNotification() {
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE_FOR_LAUNCH_APP_PENDING_INTENT,
            intent,
            flags
        )

        val contentText = "The king has been crowned."

        val notification = NotificationCompat.Builder(context, notificationChannels.breakingNewsNotificationChannelId)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setContentTitle(context.getString(R.string.breaking_news_notification_content_title))
            .setContentText(contentText)
            .setOngoing(false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.notification_small_icon)
            .setTicker(contentText)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        notificationManager.notify(BREAKING_NEWS_NOTIFICATION_ID, notification)
    }

    private companion object {
        private const val REQUEST_CODE_FOR_LAUNCH_APP_PENDING_INTENT = 0
        private const val BREAKING_NEWS_NOTIFICATION_ID = 1
    }
}