package com.toolslab.noisepercolator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsBroadcastReceiver : BroadcastReceiver() {

    private val tag = "SmsBroadcastReceiver"

    var smsExtracter = SmsExtracter() // TODO make inmutable, but how to test that way?
    var incomingSmsNotifier = IncomingSmsNotifier()

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "Received intent: " + intent.action)

        val smsMessages = smsExtracter.extractFrom(intent)
        if (smsMessages.isNotEmpty()) {
            incomingSmsNotifier.maybeNotify(context, smsMessages)
        } else {
            Log.e(tag, "No messages in intent")
        }
    }

}
