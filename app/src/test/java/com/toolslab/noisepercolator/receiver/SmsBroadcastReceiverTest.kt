package com.toolslab.noisepercolator.receiver

import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions


class SmsBroadcastReceiverTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockSmsExtracter: SmsExtracter = mock()
    private val mockIncomingSmsNotifier: IncomingSmsNotifier = mock()
    private val mockSmsMessages: List<SmsMessage> = mock()

    private val underTest: SmsBroadcastReceiver = SmsBroadcastReceiver()

    @Before
    fun setUp() {
        underTest.smsExtracter = mockSmsExtracter
        underTest.incomingSmsNotifier = mockIncomingSmsNotifier
    }

    @Test
    fun onReceive() {
        whenever(mockSmsExtracter.extractFrom(mockIntent)).thenReturn(mockSmsMessages)

        underTest.onReceive(mockContext, mockIntent)

        verify(mockSmsExtracter).extractFrom(mockIntent)
        verify(mockIncomingSmsNotifier).maybeNotify(mockContext, mockSmsMessages)
    }

    @Test
    fun onReceiveWithNoMessages() {
        whenever(mockSmsExtracter.extractFrom(mockIntent)).thenReturn(emptyList())

        underTest.onReceive(mockContext, mockIntent)

        verify(mockSmsExtracter).extractFrom(mockIntent)
        verifyZeroInteractions(mockIncomingSmsNotifier)
    }

}
