package com.example.webtonative.notifications

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.notificationDataStore by preferencesDataStore(
    name = "notification_preferences"
)

class NotificationPreferences(
    private val context: Context
) {
    companion object {
        private val LAST_NOTIFICATION_DATE =
            stringPreferencesKey("last_notification_date")
    }

    suspend fun saveLastNotificationDate(date: String) {
        context.notificationDataStore.edit {
            it[LAST_NOTIFICATION_DATE] = date
        }
    }

    suspend fun getLastNotificationDate(): String? {
        return context.notificationDataStore.data.first()[LAST_NOTIFICATION_DATE]
    }
}