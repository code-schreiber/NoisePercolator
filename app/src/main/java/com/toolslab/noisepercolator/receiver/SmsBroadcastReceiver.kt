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
        val messagesAsString = extractMessagesToListCompat(intent)
        messagesAsString.forEach { Log.d(tag, it) }
    }

    private fun extractMessagesToListCompat(intent: Intent): MutableList<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            extractMessagesToList(intent)
        } else {
            extractMessagesToListForDevicesOlderThanKitkat(intent)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun extractMessagesToList(intent: Intent): MutableList<String> {
        val messagesAsString = mutableListOf<String>()

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        messages.mapTo(messagesAsString) { createMessageAsString(it) }
        return messagesAsString
    }

    @Suppress("DEPRECATION")
    private fun extractMessagesToListForDevicesOlderThanKitkat(intent: Intent): MutableList<String> {
        val messagesAsString = mutableListOf<String>()

        val extras = intent.extras
        if (extras != null) {
            val messages = extras.get("pdus") as Array<*>
            messages
                    .map { SmsMessage.createFromPdu(it as ByteArray) }
                    .mapTo(messagesAsString) { createMessageAsString(it) }
        } else {
            Log.e(tag, "No extras in intent")
        }
        return messagesAsString
    }

    private fun createMessageAsString(message: SmsMessage): String {
        val smsBody = message.messageBody
        val address = message.originatingAddress

        return "SMS From: " + address + "\n" + smsBody
    }

}
