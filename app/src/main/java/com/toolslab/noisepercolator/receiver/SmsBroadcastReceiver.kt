package com.toolslab.noisepercolator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.notification.Notifier
import com.toolslab.noisepercolator.util.IdProvider
import timber.log.Timber


class SmsBroadcastReceiver(private var notifier: Notifier = Notifier(),
                           private val filteredOutSmsSaver: FilteredOutSmsSaver = FilteredOutSmsSaver(),
                           private val smsMessagesConverter: SmsMessagesConverter = SmsMessagesConverter(),
                           private val smsFilter: SmsFilter = SmsFilter(),
                           private val idProvider: IdProvider = IdProvider())
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received intent: $intent")
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        val smsMessages = smsMessagesConverter.convertFrom(intent)
        if (smsMessages.isNotEmpty()) {
            // Show notification for all messages that where not filtered out
            smsMessages
                    .filter { smsFilter.shouldNotify(it) }
                    .forEach { postNotification(it) }

            // Save information about messages that where filtered out
            smsMessages.filter { !smsFilter.shouldNotify(it) }
                    .forEach { filteredOutSmsSaver.saveFilteredOutSmsMessage(it) }
        } else {
            Timber.e("No messages in intent")
        }
    }

    private fun postNotification(smsMessage: SmsMessage) {
        val notificationId = idProvider.createIdFrom(smsMessage.displayOriginatingAddress)
        val title = smsMessage.displayOriginatingAddress
        val text = smsMessage.displayMessageBody
        notifier.postNotification(notificationId, title, text)
    }

}
