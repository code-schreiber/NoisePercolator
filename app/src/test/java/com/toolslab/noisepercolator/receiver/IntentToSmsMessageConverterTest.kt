package com.toolslab.noisepercolator.receiver

import android.content.Intent
import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Test

class IntentToSmsMessageConverterTest {

    private val mockIntent: Intent = mock()
    private val mockExtras: Bundle = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: IntentToSmsMessageConverter = IntentToSmsMessageConverter(mockSdkChecker)

    @Test
    fun extractEmptyExtrasLegacy() {
        val pdus: Array<ByteArray> = emptyArray()
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(mockExtras)
        whenever(mockExtras.get(IntentToSmsMessageConverter.PDUS_KEY)).thenReturn(pdus)

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual emptyList()
    }

    @Test
    fun extractFromNoExtrasLegacy() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(null)

        val result = underTest.extractFrom(mockIntent)

        result shouldEqual emptyList()
    }

}
