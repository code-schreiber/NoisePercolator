package com.toolslab.noisepercolator.filter

import android.support.annotation.VisibleForTesting
import android.telephony.SmsMessage

class SmsFilter {

    companion object {

        @VisibleForTesting
        val SPAM_KEYWORDS = listOf(
                "@",
                "bit.ly",
                "tinyurl.com",
                ".com",
                "ld.com",
                "jsagh.com",
                "psiuoh.com",
                "ysdrai.com",
                "qawrrw.com",
                "qqldgf.com",
                "eatezd.com",
                "Mastercard")
    }

    fun isNotSpam(smsMessage: SmsMessage): Boolean {
        return !isSpam(smsMessage)
    }

    fun isSpam(smsMessage: SmsMessage): Boolean {
        return bodyContainsSpam(smsMessage.displayMessageBody)
    }

    private fun bodyContainsSpam(body: String): Boolean {
        return SPAM_KEYWORDS.any { body.contains(it, true) }
    }

}
