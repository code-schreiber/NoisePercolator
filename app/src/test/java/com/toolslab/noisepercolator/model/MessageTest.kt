package com.toolslab.noisepercolator.model

import org.amshove.kluent.shouldEqual
import org.junit.Test
import java.text.DateFormat
import java.util.*

class MessageTest {

    private companion object {
        private const val ADDRESS = "an address"
        private const val DATE_IN_MILLIS = 946681199000
        private const val BODY = "a message body"
        private val FORMATTED_DATE = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(DATE_IN_MILLIS))
    }

    private val underTest = Message(ADDRESS, DATE_IN_MILLIS, BODY)

    @Test
    fun fieldsAreSetCorrectly() {
        underTest.apply {
            address shouldEqual ADDRESS
            date shouldEqual DATE_IN_MILLIS
            body shouldEqual BODY
        }
    }

    @Test
    fun getFormattedDate() {
        val result = underTest.getFormattedDate()

        result shouldEqual FORMATTED_DATE
    }

}
