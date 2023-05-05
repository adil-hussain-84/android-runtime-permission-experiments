package com.tazkiyatech.app

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

/**
 * Methods for creating and checking the status of notification channels.
 */
class NotificationChannels(private val context: Context) {

    val breakingNewsNotificationChannelId = "breaking_news"

    private val notificationManagerCompat: NotificationManagerCompat
        get() = NotificationManagerCompat.from(context)

    private val notificationChannelGroups: NotificationChannelGroups
        get() = NotificationChannelGroups(context)

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createUpdateYourProgressNotificationChannel()
        }
    }

    fun isBreakingNewsNotificationChannelFullyEnabled(): Boolean {
        return notificationChannelGroups.isNewsNotificationChannelGroupFullyEnabled() && isBreakingNewsNotificationChannelEnabled()
    }

    fun isBreakingNewsNotificationChannelEnabled(): Boolean {
        val notificationChannel = notificationManagerCompat.getNotificationChannelCompat(
            breakingNewsNotificationChannelId
        )
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.N || notificationChannel == null || notificationChannel.importance != NotificationManager.IMPORTANCE_NONE
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createUpdateYourProgressNotificationChannel() {
        val channelId = breakingNewsNotificationChannelId
        val channelName = context.getString(R.string.breaking_news_notification_channel_name)
        val channelDescription = context.getString(R.string.breaking_news_notification_channel_description)

        val notificationChannel = NotificationChannel(channelId, channelName, IMPORTANCE_LOW)

        notificationChannel.description = channelDescription
        notificationChannel.group = notificationChannelGroups.newsNotificationChannelGroupId
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)

        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }
}
