package com.toolslab.noisepercolator.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.util.device.SdkChecker

// TODO test class
class Notifier(private val context: Context = NoisePercolator.applicationContext(), // TODO ASK how to get context in a better way?
               private val sdkChecker: SdkChecker = SdkChecker()) {

    private val channelId = "defaultChannel"

    fun postNotification(notificationId: Int, title: String, text: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (sdkChecker.deviceIsOreoOrAbove()) {
            // If you target Android 8.0 (API level 26)
            // and post a notification without specifying a valid notifications channel,
            // the notification fails to post and the system logs an error.
            createNotificationChannel(notificationManager)
        }

        val notification = createNotification(title, text)
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotification(title: String, text: String): Notification {
        return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .build()
    }

    @TargetApi(SdkChecker.OREO)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "channel name"
        val description = "user-visible description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, name, importance)
        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }

}
