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
    fun defaultFieldsAreSetCorrectly() {
        val message = Message()

        message.apply {
            address shouldEqual ""
            date shouldEqual 0
            body shouldEqual ""
        }
    }

    @Test
    fun getFormattedDate() {
        val result = underTest.getFormattedDate()

        result shouldEqual FORMATTED_DATE
    }

    @Test
    fun sortedSortsNewestFirst() {
        val oneDay = 1000 * 60 * 60 * 24
        val olderMessage = Message(ADDRESS, 0, BODY)
        val message = Message(ADDRESS, DATE_IN_MILLIS, BODY)
        val newerMessage = Message(ADDRESS, DATE_IN_MILLIS + oneDay, BODY)
        val messages = listOf(newerMessage, olderMessage, message)

        messages[0] shouldEqual newerMessage
        messages[1] shouldEqual olderMessage
        messages[2] shouldEqual message

        val sortedMessages = messages.sorted()

        sortedMessages[0] shouldEqual newerMessage
        sortedMessages[1] shouldEqual message
        sortedMessages[2] shouldEqual olderMessage
    }

}
