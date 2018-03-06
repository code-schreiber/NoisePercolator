package com.toolslab.noisepercolator.view.messages

import android.support.annotation.CheckResult
import android.telephony.SmsMessage
import com.google.gson.Gson
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
        return toJsonString(message)
    }

    @CheckResult
    internal fun convert(messageAsString: String): Message {
        return fromJsonString(messageAsString, Message::class.java)
    }

    private fun <T> toJsonString(json: T): String {
        return Gson().toJson(json)
    }

    private fun <T> fromJsonString(jsonString: String, classOfT: Class<T>): T {
        return Gson().fromJson(jsonString, classOfT)
    }

}
