package com.toolslab.noisepercolator.model

import java.text.DateFormat
import java.util.*

data class Message(internal val address: String,
                   internal val date: Long,
                   internal val body: String) {

    fun getFormattedDate(): String = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(date))

}
