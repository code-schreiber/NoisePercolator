package com.toolslab.noisepercolator.notification

import android.telephony.SmsMessage

class SmsReceivedNotifier(private val notifier: Notifier = Notifier()) {

    fun postNotification(smsMessage: SmsMessage) {
        val notificationId = 123 // TODO
        val title = smsMessage.displayOriginatingAddress
        val text = smsMessage.messageBody + "-" + smsMessage.displayMessageBody
        notifier.postNotification(notificationId, title, text)
    }

}
