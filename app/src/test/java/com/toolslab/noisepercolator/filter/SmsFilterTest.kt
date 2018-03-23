package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Assert.fail
import org.junit.Test

class SmsFilterTest {

    companion object {
        const val A_SMS_BODY = "another word"
        const val A_SPAM_SMS_BODY = "bit.ly"
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

    @Test
    fun isSpamUpperCase() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn(A_SPAM_SMS_BODY.toUpperCase())

        val isSpam = underTest.isSpam(mockSmsMessage)

        isSpam shouldEqual true
    }

    @Test
    fun isSpamLowerCase() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn(A_SPAM_SMS_BODY.toLowerCase())

        val isSpam = underTest.isSpam(mockSmsMessage)

        isSpam shouldEqual true
    }

    @Test
    fun testSpamSmsList() {
        val spamMessages: List<SmsMessage> = getSpamMessages()
        for (spamMessage in spamMessages) {
            if (!underTest.isSpam(spamMessage)) {
                val smsBody = spamMessage.displayMessageBody
                fail("$smsBody should be spam")
            }
        }
    }

    private fun getSpamMessages(): List<SmsMessage> {
        val mockSmsMessages = mutableListOf<SmsMessage>()
        for (parsedSms in XmlParser().parseSpamMessages()) {
            val mockSms: SmsMessage = mock()
            whenever(mockSms.displayOriginatingAddress).thenReturn(parsedSms.address)
            whenever(mockSms.displayMessageBody).thenReturn(parsedSms.body)
            mockSmsMessages.add(mockSms)
        }
        return mockSmsMessages
    }

}

