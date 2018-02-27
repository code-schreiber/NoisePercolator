package com.toolslab.noisepercolator.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.support.annotation.VisibleForTesting
import com.toolslab.noisepercolator.util.device.SdkChecker

class NotificationChannelCreator(private val sdkChecker: SdkChecker = SdkChecker()) {

    @VisibleForTesting
    @TargetApi(SdkChecker.OREO)
    companion object {
        private const val CHANNEL_NAME = "Default channel"

        @VisibleForTesting
        const val CHANNEL_DESCRIPTION = "This is the default channel"
    }

    fun createNotificationChannel(notificationManager: NotificationManager, channelId: String) {
        if (sdkChecker.deviceIsOreoOrAbove()) {
            // If you target Android 8.0 (API level 26)
            // and post a notification without specifying a valid notifications channel,
            // the notification fails to post and the system logs an error.
            createChannel(notificationManager, channelId)
        }
    }

    @TargetApi(SdkChecker.OREO)
    private fun createChannel(notificationManager: NotificationManager, channelId: String) {
        val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        createChannel(channel, notificationManager)
    }

    @VisibleForTesting
    @TargetApi(SdkChecker.OREO)
    fun createChannel(channel: NotificationChannel, notificationManager: NotificationManager) {
        channel.description = CHANNEL_DESCRIPTION
        notificationManager.createNotificationChannel(channel)
    }

}
