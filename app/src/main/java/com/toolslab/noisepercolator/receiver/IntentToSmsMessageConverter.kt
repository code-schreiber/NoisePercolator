package com.toolslab.noisepercolator.receiver

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.util.device.SdkChecker

class IntentToSmsMessageConverter {

    companion object {
        private const val PDUS_KEY = "pdus"
    }

    @TargetApi(SdkChecker.KITKAT)
    fun convert(intent: Intent): List<SmsMessage> {
        return Telephony.Sms.Intents.getMessagesFromIntent(intent).toList()
    }

    @Suppress("DEPRECATION")
    fun convertLegacy(bundle: Bundle): List<SmsMessage> {
        val smsMessages = mutableListOf<SmsMessage>()
        val messages = bundle.get(PDUS_KEY) as Array<*>
        messages
                .map { SmsMessage.createFromPdu(it as ByteArray) }
                .mapTo(smsMessages) { it }
        return smsMessages.toList()
    }

}
