package com.toolslab.noisepercolator.receiver

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.R

class Notifier(private val context: Context) {

    private val channelId = "channel_01"

    fun notifyWith(smsMessage: SmsMessage) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // If you target Android 8.0 (API level 26)
            // and post a notification without specifying a valid notifications channel,
            // the notification fails to post and the system logs an error.
            createNotificationChannel(notificationManager)
        }

        val notificationId = smsMessage.hashCode()
        val notification = createNotificationFromSms(smsMessage)

        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationFromSms(smsMessage: SmsMessage): Notification {
        val address = smsMessage.originatingAddress
        val smsBody = smsMessage.messageBody

        return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(address)
                .setContentText(smsBody)
                .build()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "channel name"
        val description = "user-visible description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

}
