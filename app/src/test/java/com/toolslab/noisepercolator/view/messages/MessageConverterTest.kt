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
        private const val DATE_IN_MILLIS = 946681199000
        private const val BODY = "a message body"
        private val MESSAGE = Message(ADDRESS, DATE_IN_MILLIS, BODY)
        private const val MESSAGE_AS_JSON_STRING = "{" +
                "\"address\":\"" + ADDRESS + "\"," +
                "\"date\":" + DATE_IN_MILLIS + "," +
                "\"body\":\"" + BODY +
                "\"}"
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
        val result = underTest.convert(MESSAGE)

        result shouldEqual MESSAGE_AS_JSON_STRING
    }

    @Test
    fun convertString() {
        val result = underTest.convert(MESSAGE_AS_JSON_STRING)

        result.apply {
            address shouldEqual MESSAGE.address
            date shouldEqual MESSAGE.date
            body shouldEqual MESSAGE.body
        }
    }

    @Test
    fun convertToStringAndBack() {
        val originalInput = MESSAGE

        val messageAsString = underTest.convert(originalInput)
        val result = underTest.convert(messageAsString)

        result.apply {
            address shouldEqual originalInput.address
            date shouldEqual originalInput.date
            body shouldEqual originalInput.body
        }
    }

    @Test
    fun convertToMessageAndBack() {
        val originalInput = MESSAGE_AS_JSON_STRING

        val message = underTest.convert(originalInput)
        val result = underTest.convert(message)

        result shouldEqual originalInput
    }

}
