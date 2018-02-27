package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage
import com.toolslab.noisepercolator.model.Message

class SmsFilter {

    fun shouldNotify(smsMessage: SmsMessage): Boolean {
        return !smsMessage.displayMessageBody.contains("a keyword")
    }

    fun isSpam(message: Message): Boolean {
        return message.body.contains("a keyword")
    }

}
