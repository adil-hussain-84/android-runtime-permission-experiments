package com.tazkiyatech.app

import android.annotation.TargetApi
import android.app.NotificationChannelGroup
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.app.NotificationManagerCompat

/**
 * Methods for creating and checking the status of notification channel groups.
 */
class NotificationChannelGroups(private val context: Context) {

    val newsNotificationChannelGroupId = "news"

    private val notificationManagerCompat: NotificationManagerCompat
        get() = NotificationManagerCompat.from(context)

    fun createNotificationChannelGroups() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewsNotificationChannelGroup()
        }
    }

    fun isNewsNotificationChannelGroupFullyEnabled(): Boolean {
        return notificationManagerCompat.areNotificationsEnabled() && isNewsNotificationChannelGroupEnabled()
    }

    fun isNewsNotificationChannelGroupEnabled(): Boolean {
        val notificationChannelGroup = notificationManagerCompat.getNotificationChannelGroupCompat(
            newsNotificationChannelGroupId
        )
        return notificationChannelGroup == null || !notificationChannelGroup.isBlocked
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNewsNotificationChannelGroup() {
        val notificationChannelGroup = NotificationChannelGroup(
            newsNotificationChannelGroupId,
            context.getString(R.string.news_notification_channel_group_name)
        )

        notificationManagerCompat.createNotificationChannelGroup(notificationChannelGroup)
    }
}