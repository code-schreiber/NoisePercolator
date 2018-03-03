package com.toolslab.noisepercolator.db

import android.telephony.SmsMessage
import com.toolslab.noisepercolator.view.messages.MessageConverter

class FilteredOutSmsSaver(private val dataProvider: DataProvider = DataProvider(),
                          private val messageConverter: MessageConverter = MessageConverter()) {

    fun saveFilteredOutSmsMessage(filteredOutSmsMessage: SmsMessage) {
        val message = messageConverter.convert(filteredOutSmsMessage)
        dataProvider.saveMessage(message)
    }

}
