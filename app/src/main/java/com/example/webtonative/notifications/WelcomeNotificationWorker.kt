package com.example.webtonative.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.time.LocalDate
import java.util.concurrent.TimeUnit

const val TAG = "WelcomeNotification"

class WelcomeNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "Worker started")

        val prefs = NotificationPreferences(applicationContext)

        val today = LocalDate.now().toString()

        Log.d(TAG, "Today = $today")

        if (prefs.getLastNotificationDate() == today) {
            Log.d(TAG, "Notification already shown today")
            return Result.success()
        }

        val isForeground =
            ProcessLifecycleOwner.get()
                .lifecycle
                .currentState
                .isAtLeast(Lifecycle.State.STARTED)

        Log.d(TAG, "App in foreground = $isForeground")

        if (isForeground) {
            Log.d(TAG, "Skipping notification because app is open")
            return Result.success()
        }

        Log.d(TAG, "Showing notification")

        val shown = NotificationHelper.showWelcomeNotification(applicationContext)

        Log.d(TAG, "Notification shown = $shown")

        if (shown) {
            prefs.saveLastNotificationDate(today)
            Log.d(TAG, "Saved notification date = $today")
        }

        return Result.success()
    }

    companion object {
        fun schedule(context: Context) {
            Log.d(TAG, "Scheduling notification worker")

            val request =
                OneTimeWorkRequestBuilder<WelcomeNotificationWorker>()
                    .setInitialDelay(1,TimeUnit.MINUTES)
                    .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    "daily_notification",
                    ExistingWorkPolicy.KEEP,
                    request
                )
        }
    }
}