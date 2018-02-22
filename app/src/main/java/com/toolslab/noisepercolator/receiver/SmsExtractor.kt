package com.toolslab.noisepercolator.receiver

import android.content.Intent
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber

class SmsExtractor(private val intentToSmsMessageConverter: IntentToSmsMessageConverter = IntentToSmsMessageConverter(),
                   private val sdkChecker: SdkChecker = SdkChecker()) {

    fun extractFrom(intent: Intent): List<SmsMessage> {
        return when {
            sdkChecker.deviceIsKitkatOrAbove() -> intentToSmsMessageConverter.convert(intent)
            intent.extras != null -> intentToSmsMessageConverter.convertLegacy(intent.extras)
            else -> {
                Timber.e("No extras in intent")
                emptyList()
            }
        }
    }

}
