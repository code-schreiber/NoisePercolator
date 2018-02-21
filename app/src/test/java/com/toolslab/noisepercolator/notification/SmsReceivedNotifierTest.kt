package com.toolslab.noisepercolator.notification

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.mockito.Mockito.verify

class SmsReceivedNotifierTest {

    private val mockNotifier: Notifier = mock()

    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: SmsReceivedNotifier = SmsReceivedNotifier(mockNotifier)

    @Test
    fun postNotification() {
        val expectedNotificationId = 123
        val expectedTitle = "test will fail"
        val expectedText = "expected text"
        whenever(mockSmsMessage.originatingAddress).thenReturn(expectedTitle)
        whenever(mockSmsMessage.messageBody).thenReturn(expectedText)

        underTest.postNotification(mockSmsMessage)

        verify(mockNotifier).postNotification(expectedNotificationId, expectedTitle, expectedText)
    }

}
