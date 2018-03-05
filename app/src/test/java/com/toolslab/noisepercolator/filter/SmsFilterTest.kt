package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Test

class SmsFilterTest {

    companion object {
        const val A_SMS_BODY = "another word"
    }

    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: SmsFilter = SmsFilter()

    @Test
    fun isSpamForKeywords() {
        for (keyword in SmsFilter.SPAM_KEYWORDS) {
            whenever(mockSmsMessage.displayMessageBody).thenReturn(keyword)

            val isSpam = underTest.isSpam(mockSmsMessage)

            isSpam shouldEqual true
        }
    }

    @Test
    fun isNotSpamForKeywords() {
        for (keyword in SmsFilter.SPAM_KEYWORDS) {
            whenever(mockSmsMessage.displayMessageBody).thenReturn(keyword)

            val isNotSpam = underTest.isNotSpam(mockSmsMessage)

            isNotSpam shouldEqual false
        }
    }

    @Test
    fun isSpam() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn(A_SMS_BODY)

        val isSpam = underTest.isSpam(mockSmsMessage)

        isSpam shouldEqual false
    }

    @Test
    fun isNotSpam() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn(A_SMS_BODY)

        val isNotSpam = underTest.isNotSpam(mockSmsMessage)

        isNotSpam shouldEqual true


    }

}
