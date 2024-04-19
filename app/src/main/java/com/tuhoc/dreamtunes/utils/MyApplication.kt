package com.tuhoc.dreamtunes.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.tuhoc.dreamtunes.utils.Constants.CHANNEL_ID

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        createChannelNotification()
    }

    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "Now Playing", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.setSound(null, null)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}