package com.toolslab.noisepercolator.notification

import android.app.NotificationManager
import android.content.Context
import android.support.annotation.VisibleForTesting
import com.toolslab.noisepercolator.NoisePercolator

class Notifier(private val context: Context = NoisePercolator.applicationContext(),
               private val notificationChannelCreator: NotificationChannelCreator = NotificationChannelCreator(),
               private val notificationCreator: NotificationCreator = NotificationCreator()) {

    @VisibleForTesting
    companion object {
        @VisibleForTesting
        const val CHANNEL_ID = "defaultChannel"
    }

    fun postNotification(notificationId: Int, title: String, text: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationChannelCreator.createNotificationChannel(notificationManager, CHANNEL_ID)
        val notification = notificationCreator.createNotification(context, CHANNEL_ID, title, text)
        notificationManager.notify(notificationId, notification)
    }

}
