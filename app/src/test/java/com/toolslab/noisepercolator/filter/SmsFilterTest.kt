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
    fun shouldNotify() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("another word")

        val result = underTest.shouldNotify(mockSmsMessage)

        result shouldEqual true
    }

    @Test
    fun shouldNotNotify() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("a keyword")

        val result = underTest.shouldNotify(mockSmsMessage)

        result shouldEqual false
    }

}
