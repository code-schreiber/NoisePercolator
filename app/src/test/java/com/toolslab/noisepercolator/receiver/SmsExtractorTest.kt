package com.toolslab.noisepercolator.receiver

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Test

class SmsExtractorTest {

    private val mockIntent: Intent = mock()
    private val mockExtras: Bundle = mock()
    private val mockSmsMessage: SmsMessage = mock()
    private val mockIntentToSmsMessageConverter: IntentToSmsMessageConverter = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: SmsExtractor = SmsExtractor(mockIntentToSmsMessageConverter, mockSdkChecker)

    @Test
    fun extractFrom() {
        val messages = listOf(mockSmsMessage)
        val expected = messages.toList()
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(true)
        whenever(mockIntentToSmsMessageConverter.convert(mockIntent)).thenReturn(messages)

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual expected
    }

    @Test
    fun extractFromEmpty() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(true)
        whenever(mockIntentToSmsMessageConverter.convert(mockIntent)).thenReturn(emptyList())

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual emptyList()
    }

    @Test
    fun extractFromLegacy() {
        val messages = listOf(mockSmsMessage)
        val expected = messages.toList()
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(mockExtras)
        whenever(mockIntentToSmsMessageConverter.convertLegacy(mockExtras)).thenReturn(messages)

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual expected
    }

    @Test
    fun extractFromEmptyLegacy() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(mockExtras)
        whenever(mockIntentToSmsMessageConverter.convertLegacy(mockExtras)).thenReturn(emptyList())

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual emptyList()
    }

    @Test
    fun extractFromNoExtrasLegacy() {
        whenever(mockIntent.extras).thenReturn(null)
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)

        val result = underTest.extractFrom(mockIntent)

        verifyZeroInteractions(mockIntentToSmsMessageConverter)
        result shouldEqual emptyList()
    }

}
