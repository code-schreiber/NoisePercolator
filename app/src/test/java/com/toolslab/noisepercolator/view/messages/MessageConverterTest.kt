package com.toolslab.noisepercolator.view.messages

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Test

class MessageConverterTest {

    companion object {
        const val ADDRESS = "an address"
        const val DATE_IN_MILLIS = 946681199000
        const val BODY = "a message body"
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

}
