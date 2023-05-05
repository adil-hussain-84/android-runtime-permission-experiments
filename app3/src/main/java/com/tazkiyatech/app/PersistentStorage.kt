package com.tazkiyatech.app

import android.content.Context
import android.content.SharedPreferences

class PersistentStorage(private val context: Context) {

    var userHasAcknowledgedPostNotificationsPermissionRationale: Boolean
        get() = getSharedPreferences()
            .getBoolean(USER_HAS_ACKNOWLEDGED_POST_NOTIFICATIONS_PERMISSION_RATIONALE_KEY, false)
        set(value) = getSharedPreferences()
            .edit()
            .putBoolean(USER_HAS_ACKNOWLEDGED_POST_NOTIFICATIONS_PERMISSION_RATIONALE_KEY, value)
            .apply()

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    private companion object {
        private const val USER_HAS_ACKNOWLEDGED_POST_NOTIFICATIONS_PERMISSION_RATIONALE_KEY =
            "USER_HAS_ACKNOWLEDGED_POST_NOTIFICATIONS_PERMISSION_RATIONALE"
    }
}
