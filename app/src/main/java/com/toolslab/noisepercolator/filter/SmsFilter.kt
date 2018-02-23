package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage

class SmsFilter {

    fun shouldNotify(smsMessage: SmsMessage): Boolean {
        return !smsMessage.displayMessageBody.contains("a keyword")
    }

}
