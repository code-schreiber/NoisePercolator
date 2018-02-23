package com.toolslab.noisepercolator.receiver

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.support.annotation.VisibleForTesting
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber

// TODO rename
class IntentToSmsMessageConverter(private val sdkChecker: SdkChecker = SdkChecker()) {

    @VisibleForTesting
    companion object {

        @VisibleForTesting
        const val PDUS_KEY = "pdus"
    }

    fun extractFrom(intent: Intent): List<SmsMessage> {
        return when {
            sdkChecker.deviceIsKitkatOrAbove() -> convert(intent)
            intent.extras != null -> convertLegacy(intent.extras)
            else -> {
                Timber.e("No extras in intent")
                emptyList()
            }
        }
    }

    @TargetApi(SdkChecker.KITKAT)
    private fun convert(intent: Intent): List<SmsMessage> {
        return Telephony.Sms.Intents.getMessagesFromIntent(intent).toList()
    }

    @Suppress("DEPRECATION")
    private fun convertLegacy(bundle: Bundle): List<SmsMessage> {
        val smsMessages = mutableListOf<SmsMessage>()
        val messages = bundle.get(PDUS_KEY) as Array<*>
        messages
                .map { SmsMessage.createFromPdu(it as ByteArray) }
                .mapTo(smsMessages) { it }
        return smsMessages.toList()
    }

    @TargetApi(SdkChecker.KITKAT)
    fun getDefaultSmsPackage(context: Context): String {
        return Telephony.Sms.getDefaultSmsPackage(context)
    }

}
