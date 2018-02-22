package com.toolslab.noisepercolator.db

import android.telephony.SmsMessage
import timber.log.Timber

class FilteredOutSmsSaver {

    fun saveFilteredOutSmsMessage(filteredOutSmsMessage: SmsMessage) {
        // TODO Save information about messages that where filtered out in a way that they can be shown
        Timber.d("TODO Save information about message that where filtered out: " + filteredOutSmsMessage)
    }

}
