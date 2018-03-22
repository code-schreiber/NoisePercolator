package com.toolslab.noisepercolator.receiver

import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.notification.Notifier
import org.junit.Test


class SmsBroadcastReceiverTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockNotifier: Notifier = mock()
    private val mockSmsMessagesConverter: SmsMessagesConverter = mock()
    private val mockSmsFilter: SmsFilter = mock()
    private val mockFilteredOutSmsSaver: FilteredOutSmsSaver = mock()
    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: SmsBroadcastReceiver =
            SmsBroadcastReceiver(mockNotifier, mockFilteredOutSmsSaver, mockSmsMessagesConverter, mockSmsFilter)

    @Test
    fun onReceiveWithNoMessages() {
        whenever(mockSmsMessagesConverter.convertFrom(mockIntent)).thenReturn(emptyList())

        underTest.onReceive(mockContext, mockIntent)

        verifyZeroInteractions(mockSmsFilter)
        verifyZeroInteractions(mockNotifier)
        verifyZeroInteractions(mockFilteredOutSmsSaver)
    }

    @Test
    fun onReceiveNotifying() {
        val notificationId = 123456789
        val title = "+1 (23) 456 789"
        val text = "the display message body"
        whenever(mockSmsMessage.displayOriginatingAddress).thenReturn(title)
        whenever(mockSmsMessage.displayMessageBody).thenReturn(text)
        whenever(mockSmsMessagesConverter.convertFrom(mockIntent)).thenReturn(listOf(mockSmsMessage))
        whenever(mockSmsFilter.isNotSpam(mockSmsMessage)).thenReturn(true)

        underTest.onReceive(mockContext, mockIntent)

        verify(mockNotifier).postNotification(notificationId, title, text)
        verifyZeroInteractions(mockFilteredOutSmsSaver)
    }

    @Test
    fun onReceiveNotNotifying() {
        whenever(mockSmsMessagesConverter.convertFrom(mockIntent)).thenReturn(listOf(mockSmsMessage))
        whenever(mockSmsFilter.isNotSpam(mockSmsMessage)).thenReturn(false)

        underTest.onReceive(mockContext, mockIntent)

        verifyZeroInteractions(mockNotifier)
        verify(mockFilteredOutSmsSaver).saveFilteredOutSmsMessage(mockSmsMessage)
    }

}
