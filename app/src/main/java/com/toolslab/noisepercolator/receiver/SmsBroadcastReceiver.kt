package com.toolslab.noisepercolator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.annotation.VisibleForTesting
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.notification.Notifier
import timber.log.Timber
import java.util.*


class SmsBroadcastReceiver(private var notifier: Notifier = Notifier(),
                           private val filteredOutSmsSaver: FilteredOutSmsSaver = FilteredOutSmsSaver(),
                           private val intentToSmsMessageConverter: IntentToSmsMessageConverter = IntentToSmsMessageConverter(),
                           private val smsFilter: SmsFilter = SmsFilter())
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received intent: $intent.action")
        processIntent(intent)
    }

    private fun processIntent(intent: Intent) {
        val smsMessages = intentToSmsMessageConverter.extractFrom(intent)
        if (smsMessages.isNotEmpty()) {
            // Show notification for all messages that where not filtered out
            smsMessages
                    .filter { smsFilter.shouldNotify(it) }
                    .forEach { postNotification(it) }

            // Save information about messages that where filtered out
            smsMessages.
                    filter { !smsFilter.shouldNotify(it) }
                    .forEach { filteredOutSmsSaver.saveFilteredOutSmsMessage(it) }
        } else {
            Timber.e("No messages in intent")
        }
    }

    private fun postNotification(smsMessage: SmsMessage) {
        val notificationId = createIdFrom(smsMessage.displayOriginatingAddress)
        val title = smsMessage.displayOriginatingAddress
        val text = smsMessage.displayMessageBody
        notifier.postNotification(notificationId, title, text)
    }

    @VisibleForTesting
    fun createIdFrom(displayOriginatingAddress: String): Int {
        val leaveOnlyNumbersRegex = "\\D+".toRegex()
        val onlyNumbers = displayOriginatingAddress.replace(leaveOnlyNumbersRegex, "")
        if (onlyNumbers.isNotEmpty()) {
            val maxLength = Int.MAX_VALUE.toString().length - 1
            val startIndex = Math.max(onlyNumbers.length, maxLength) - maxLength
            return onlyNumbers.substring(startIndex).toInt()
        }
        return Random().nextInt()
    }

}
