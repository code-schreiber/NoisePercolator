package com.toolslab.noisepercolator.filter

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class CustomFilterTest {

    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: CustomFilter = CustomFilter()

    @Test
    fun shouldNotify() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("another word")

        val result = underTest.shouldNotify(mockSmsMessage)

        assertThat(result, `is`(true))
    }

    @Test
    fun shouldNotNotify() {
        whenever(mockSmsMessage.displayMessageBody).thenReturn("a keyword")

        val result = underTest.shouldNotify(mockSmsMessage)

        assertThat(result, `is`(false))
    }

}
