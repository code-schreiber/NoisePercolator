package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.NotificationCompat
import com.toolslab.noisepercolator.R

class NotificationCreator {

    @VisibleForTesting
    companion object {
        @VisibleForTesting
        const val SMALL_ICON = R.mipmap.ic_launcher
    }

    fun createNotification(context: Context, channelId: String, title: String, text: String): Notification {
        val builder = NotificationCompat.Builder(context, channelId)
        return setNotificationAttributes(builder, title, text)
    }

    @VisibleForTesting()
    fun setNotificationAttributes(builder: NotificationCompat.Builder, title: String, text: String): Notification {
        return builder
                .setSmallIcon(SMALL_ICON)
                .setContentTitle(title)
                .setContentText(text)
                .build()
    }

}
