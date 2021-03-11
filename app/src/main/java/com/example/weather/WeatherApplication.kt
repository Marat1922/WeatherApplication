package com.example.weather

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import timber.log.Timber
import timber.log.Timber.DebugTree


class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        onCreateNotificationChannel()
        Timber.plant(DebugTree())
    }

    private fun onCreateNotificationChannel() {
        val notificationChannel: NotificationChannel
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(CHANNEL_ID)
            notificationChannel = NotificationChannel(CHANNEL_ID, DESCRIPTION, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "com.example.weather"
        const val DESCRIPTION = "Test notification"
    }
}