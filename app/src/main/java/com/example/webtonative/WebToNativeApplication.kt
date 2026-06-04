package com.example.webtonative

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.webtonative.notifications.AppLifecycleObserver
import com.example.webtonative.notifications.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WebToNativeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createNotificationChannel(this)

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(
                AppLifecycleObserver(this)
            )
    }
}