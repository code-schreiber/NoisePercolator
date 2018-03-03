package com.toolslab.noisepercolator.receiver

import android.content.Intent
import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Test

class SmsMessagesConverterTest {

    private val mockIntent: Intent = mock()
    private val mockExtras: Bundle = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: SmsMessagesConverter = SmsMessagesConverter(mockSdkChecker)

    @Test
    fun convertFromEmptyExtrasLegacy() {
        val pdus: Array<ByteArray> = emptyArray()
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(mockExtras)
        whenever(mockExtras.get(SmsMessagesConverter.PDUS_KEY)).thenReturn(pdus)

        val result = underTest.convertFrom(mockIntent)

        result shouldEqual emptyList()
    }

    @Test
    fun convertFromNullExtrasLegacy() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockIntent.extras).thenReturn(null)

        val result = underTest.convertFrom(mockIntent)

        result shouldEqual emptyList()
    }

}
