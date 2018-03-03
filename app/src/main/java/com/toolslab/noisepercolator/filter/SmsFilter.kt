package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage

class SmsFilter {

    fun isNotSpam(smsMessage: SmsMessage): Boolean {
        return !isSpam(smsMessage)
    }

    fun isSpam(smsMessage: SmsMessage): Boolean {
        return bodyContainsSpam(smsMessage.displayMessageBody)
    }

    private fun bodyContainsSpam(body: String) = body.contains("a keyword")

}
