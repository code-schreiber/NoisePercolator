package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Test

class SmsFilterTest {

    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: SmsFilter = SmsFilter()

    @Test
    fun isSpam() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("another word")

        val result = underTest.isNotSpam(mockSmsMessage)

        result shouldEqual true
    }

    @Test
    fun isNotSpam() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("a keyword")

        val result = underTest.isNotSpam(mockSmsMessage)

        result shouldEqual false
    }

}
