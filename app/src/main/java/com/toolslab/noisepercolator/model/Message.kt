package com.toolslab.noisepercolator.model

import java.text.DateFormat
import java.util.*

class Message(internal val address: String,
              internal val date: Long,
              internal val body: String,
              internal val spam: Boolean = false,
              internal val debugInfo: String) {

    fun getFormattedDate(): String = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(date))

}
