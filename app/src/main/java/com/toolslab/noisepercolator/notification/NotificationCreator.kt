package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v4.app.NotificationCompat
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.util.device.SdkChecker
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil

class NotificationCreator(private val sdkChecker: SdkChecker = SdkChecker(),
                          private val packageManagerUtil: PackageManagerUtil = PackageManagerUtil()) {

    @VisibleForTesting
    companion object {

        @VisibleForTesting
        const val SMALL_ICON = R.drawable.ic_notification

        @VisibleForTesting
        const val SMALL_ICON_LEGACY = R.mipmap.ic_launcher
    }

    fun createNotification(context: Context, channelId: String, title: String, text: String): Notification {
        val builder = NotificationCompat.Builder(context, channelId)
        return setNotificationAttributes(builder, title, text)
    }

    @VisibleForTesting()
    fun setNotificationAttributes(builder: NotificationCompat.Builder, title: String, text: String): Notification {
        val pendingIntent = packageManagerUtil.createLaunchDefaultSmsAppPendingIntent()
        return builder
                .setSmallIcon(getSmallIcon())
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()
    }

    private fun getSmallIcon() = if (sdkChecker.deviceIsNougatOrAbove()) SMALL_ICON else SMALL_ICON_LEGACY

}
