package com.toolslab.noisepercolator.view.messages

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import org.amshove.kluent.shouldEqual
import org.junit.Test

class MessageConverterTest {

    private companion object {
        private const val ADDRESS = "an address"
        private const val DATE = "946681199000"
        private const val DATE_IN_MILLIS = 946681199000
        private const val BODY = "a message body"
    }

    private val mockSmsMessage: SmsMessage = mock()

    private val underTest = MessageConverter()

    @Test
    fun convertSmsMessage() {
        whenever(mockSmsMessage.displayOriginatingAddress).thenReturn(ADDRESS)
        whenever(mockSmsMessage.timestampMillis).thenReturn(DATE_IN_MILLIS)
        whenever(mockSmsMessage.displayMessageBody).thenReturn(BODY)

        val result = underTest.convert(mockSmsMessage)

        result.apply {
            address shouldEqual ADDRESS
            date shouldEqual DATE_IN_MILLIS
            body shouldEqual BODY
        }
    }

    @Test
    fun convertMessage() {
        val message = Message(ADDRESS, DATE_IN_MILLIS, BODY, true, "a debug info")

        val result = underTest.convert(message)

        result shouldEqual ADDRESS + "_SEPARATOR_" +
                DATE + "_SEPARATOR_" +
                BODY + "_SEPARATOR_" +
                true + "_SEPARATOR_" +
                "a debug info" + "_SEPARATOR_"
    }

    @Test
    fun convertString() {
        val input = ADDRESS + "_SEPARATOR_" +
                DATE + "_SEPARATOR_" +
                BODY + "_SEPARATOR_" +
                true + "_SEPARATOR_" +
                "a debug info" + "_SEPARATOR_"

        val result = underTest.convert(input)

        result.apply {
            address shouldEqual ADDRESS
            date shouldEqual DATE_IN_MILLIS
            body shouldEqual BODY
            spam shouldEqual true
        }
    }

    @Test
    fun convertToStringAndBack() {
        val input = Message(ADDRESS, DATE_IN_MILLIS, BODY, true, "a debug info")

        val messageAsString = underTest.convert(input)
        val result = underTest.convert(messageAsString)

        result.apply {
            address shouldEqual input.address
            date shouldEqual input.date
            body shouldEqual input.body
            spam shouldEqual input.spam
            debugInfo shouldEqual input.debugInfo
        }
    }

    @Test
    fun convertToMessageAndBack() {
        val input = ADDRESS + "_SEPARATOR_" +
                DATE + "_SEPARATOR_" +
                BODY + "_SEPARATOR_" +
                true + "_SEPARATOR_" +
                "a debug info" + "_SEPARATOR_"

        val message = underTest.convert(input)
        val result = underTest.convert(message)

        result shouldEqual input
    }

}
