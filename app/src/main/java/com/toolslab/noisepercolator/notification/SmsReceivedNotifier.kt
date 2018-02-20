package com.toolslab.noisepercolator.notification

import android.telephony.SmsMessage

class SmsReceivedNotifier(private val notifier: Notifier) {

    fun postNotification(smsMessage: SmsMessage) {
        val notificationId = smsMessage.originatingAddress.toInt()
        val title = smsMessage.originatingAddress
        val text = smsMessage.messageBody
        notifier.postNotification(notificationId, title, text)
    }

}
