package com.toolslab.noisepercolator.receiver

import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions


class SmsBroadcastReceiverTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockSmsExtractor: SmsExtractor = mock()
    private val mockIncomingSmsNotifier: IncomingSmsNotifier = mock()
    private val mockSmsMessages: List<SmsMessage> = mock()

    private val underTest: SmsBroadcastReceiver = SmsBroadcastReceiver(mockSmsExtractor, mockIncomingSmsNotifier)

    @Test
    fun onReceive() {
        whenever(mockSmsExtractor.extractFrom(mockIntent)).thenReturn(mockSmsMessages)

        underTest.onReceive(mockContext, mockIntent)

        verify(mockSmsExtractor).extractFrom(mockIntent)
        verify(mockIncomingSmsNotifier).maybeNotify(mockSmsMessages)
    }

    @Test
    fun onReceiveWithNoMessages() {
        whenever(mockSmsExtractor.extractFrom(mockIntent)).thenReturn(emptyList())

        underTest.onReceive(mockContext, mockIntent)

        verify(mockSmsExtractor).extractFrom(mockIntent)
        verifyZeroInteractions(mockIncomingSmsNotifier)
    }

    @Test
    fun parameterLessConstructorExists() {
        SmsBroadcastReceiver() // If this compiles we are good to go
    }

}
