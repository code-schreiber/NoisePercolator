package com.toolslab.noisepercolator.view.messages

import android.support.annotation.CheckResult
import android.telephony.SmsMessage
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.model.Message

class MessageConverter(private val smsFilter: SmsFilter = SmsFilter()) {

    @CheckResult
    internal fun convert(smsMessage: SmsMessage): Message {
        val address = smsMessage.displayOriginatingAddress
        val date = smsMessage.timestampMillis
        val body = smsMessage.displayMessageBody
        val spam = smsFilter.isSpam(smsMessage)
        val debugInfo = smsMessage.toString()
        return Message(address, date, body, spam, debugInfo)
    }

    @CheckResult
    internal fun convert(message: Message): String {
        // TODO use json
        return message.address + "_SEPARATOR_" +
                message.date + "_SEPARATOR_" +
                message.body + "_SEPARATOR_" +
                message.spam + "_SEPARATOR_" +
                message.debugInfo + "_SEPARATOR_"
    }

    @CheckResult
    internal fun convert(messageAsString: String): Message {
        // TODO use json
        val splitted = messageAsString.split("_SEPARATOR_")
        val address = splitted[0]
        val date = splitted[1].toLong()
        val body = splitted[2]
        val spam = splitted[3].toBoolean()
        val debugInfo = splitted[4]
        val message = Message(address, date, body, spam, debugInfo)
        return message
    }

}
