package com.toolslab.noisepercolator.receiver

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log


class SmsBroadcastReceiver : BroadcastReceiver() {

    private val tag = "SmsBroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "Received intent: " + intent.action)

        val smsMessages = extractMessagesToListCompat(intent)

        val customFilter = CustomFilter()
        val notifier = Notifier(context)
        smsMessages
                .filter { customFilter.shouldNotify(it) }
                .forEach { notifier.notifyWith(it) }
    }

    private fun extractMessagesToListCompat(intent: Intent): MutableList<SmsMessage> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            extractMessagesToList(intent)
        } else {
            extractMessagesToListForDevicesOlderThanKitkat(intent)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun extractMessagesToList(intent: Intent): MutableList<SmsMessage> {
        val smsMessages = mutableListOf<SmsMessage>()
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        messages.mapTo(smsMessages) { it }
        return smsMessages
    }

    @Suppress("DEPRECATION")
    private fun extractMessagesToListForDevicesOlderThanKitkat(intent: Intent): MutableList<SmsMessage> {
        val smsMessages = mutableListOf<SmsMessage>()
        if (intent.extras != null) {
            val messages = intent.extras.get("pdus") as Array<*>
            messages
                    .map { SmsMessage.createFromPdu(it as ByteArray) }
                    .mapTo(smsMessages) { it }
        } else {
            Log.e(tag, "No extras in intent")
        }
        return smsMessages
    }

}
