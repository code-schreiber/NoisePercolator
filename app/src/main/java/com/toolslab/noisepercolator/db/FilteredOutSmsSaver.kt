package com.toolslab.noisepercolator.db

import android.telephony.SmsMessage
import timber.log.Timber

class FilteredOutSmsSaver(private val persister: Persister = Persister()) {

    fun saveFilteredOutSmsMessage(filteredOutSmsMessage: SmsMessage) {
        // TODO Save information about messages that where filtered out in a way that they can be shown
        Timber.d("TODO Save information about message that was filtered out: $filteredOutSmsMessage.displayMessageBody")
        val numberOfMessages = persister.getNumberOfMessages()
        persister.setNumberOfMessages(numberOfMessages + 1)
    }

}
