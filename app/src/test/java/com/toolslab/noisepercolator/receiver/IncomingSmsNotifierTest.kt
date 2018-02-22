package com.toolslab.noisepercolator.receiver

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.CustomFilter
import com.toolslab.noisepercolator.notification.SmsReceivedNotifier
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions

class IncomingSmsNotifierTest {

    private val mockSmsMessage: SmsMessage = mock()
    private val mockCustomFilter: CustomFilter = mock()
    private val mockSmsReceivedNotifier: SmsReceivedNotifier = mock()
    private val mockFilteredOutSmsSaver: FilteredOutSmsSaver = mock()

    private val underTest: IncomingSmsNotifier =
            IncomingSmsNotifier(mockCustomFilter, mockSmsReceivedNotifier, mockFilteredOutSmsSaver)

    @Test
    fun maybeNotifyWithNotFilteredMessage() {
        val smsMessages = listOf(mockSmsMessage)
        whenever(mockCustomFilter.shouldNotify(mockSmsMessage)).thenReturn(true)

        underTest.maybeNotify(smsMessages)

        verify(mockSmsReceivedNotifier).postNotification(mockSmsMessage)
        verifyZeroInteractions(mockFilteredOutSmsSaver)
    }

    @Test
    fun maybeNotifyWithFilteredMessage() {
        val smsMessages = listOf(mockSmsMessage)
        whenever(mockCustomFilter.shouldNotify(mockSmsMessage)).thenReturn(false)

        underTest.maybeNotify(smsMessages)

        verifyZeroInteractions(mockSmsReceivedNotifier)
        verify(mockFilteredOutSmsSaver).saveInfosAbout(mockSmsMessage)
    }

    @Test
    fun maybeNotifyWithNoMessages() {
        val smsMessages = emptyList<SmsMessage>()

        underTest.maybeNotify(smsMessages)

        verifyZeroInteractions(mockSmsReceivedNotifier)
        verifyZeroInteractions(mockFilteredOutSmsSaver)
    }

}
