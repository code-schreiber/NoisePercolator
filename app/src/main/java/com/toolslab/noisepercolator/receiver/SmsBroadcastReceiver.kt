package com.toolslab.noisepercolator.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class SmsBroadcastReceiver(private val smsExtractor: SmsExtractor = SmsExtractor(),
                           private var incomingSmsNotifier: IncomingSmsNotifier = IncomingSmsNotifier())
    : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Received intent: %s", intent.action)

        val smsMessages = smsExtractor.extractFrom(intent)
        if (smsMessages.isNotEmpty()) {
            incomingSmsNotifier.maybeNotify(smsMessages)
        } else {
            Timber.e("No messages in intent")
        }
    }

}
