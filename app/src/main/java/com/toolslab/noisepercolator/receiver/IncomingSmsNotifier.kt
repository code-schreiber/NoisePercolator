package com.toolslab.noisepercolator.receiver

import android.telephony.SmsMessage
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.CustomFilter
import com.toolslab.noisepercolator.notification.SmsReceivedNotifier

class IncomingSmsNotifier(private val customFilter: CustomFilter = CustomFilter(),
                          private val smsReceivedNotifier: SmsReceivedNotifier = SmsReceivedNotifier(),
                          private val filteredOutSmsSaver: FilteredOutSmsSaver = FilteredOutSmsSaver()) {

    fun maybeNotify(smsMessages: List<SmsMessage>) {
        // Show notification for all messages that where not filtered out
        notifyNonFilteredMessages(smsMessages)

        // Save information about messages that where filtered out
        saveInfosAboutFilteredOutMessages(smsMessages)
    }

    private fun notifyNonFilteredMessages(smsMessages: List<SmsMessage>) {
        smsMessages
                .filter { customFilter.shouldNotify(it) }
                .forEach { smsReceivedNotifier.postNotification(it) }
    }

    private fun saveInfosAboutFilteredOutMessages(smsMessages: List<SmsMessage>) {
        smsMessages.filter { !customFilter.shouldNotify(it) }
                .forEach {
                    filteredOutSmsSaver.saveFilteredOutSmsMessage(it)
                }
    }

}
