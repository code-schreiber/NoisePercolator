package com.toolslab.noisepercolator.receiver

import android.telephony.SmsMessage

class CustomFilter {

    fun shouldNotify(smsMessage: SmsMessage): Boolean {
        return !smsMessage.displayMessageBody.contains("a keyword")
    }

}
