package com.toolslab.noisepercolator.view.messages

import android.support.annotation.CheckResult
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.model.Message

class MessageConverter {

    @CheckResult
    internal fun convert(smsMessage: SmsMessage): Message {
        val address = smsMessage.displayOriginatingAddress
        val date = smsMessage.timestampMillis
        val body = smsMessage.displayMessageBody
        return Message(address, date, body)
    }

}
