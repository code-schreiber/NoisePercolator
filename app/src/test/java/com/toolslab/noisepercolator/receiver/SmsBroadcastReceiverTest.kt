package com.toolslab.noisepercolator.receiver

import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.db.FilteredOutSmsSaver
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.notification.Notifier
import org.amshove.kluent.shouldBeInRange
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions


class SmsBroadcastReceiverTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockNotifier: Notifier = mock()
    private val mockIntentToSmsMessageConverter: IntentToSmsMessageConverter = mock()
    private val mockSmsFilter: SmsFilter = mock()
    private val mockFilteredOutSmsSaver: FilteredOutSmsSaver = mock()
    private val mockSmsMessage: SmsMessage = mock()

    private val underTest: SmsBroadcastReceiver =
            SmsBroadcastReceiver(mockNotifier, mockFilteredOutSmsSaver, mockIntentToSmsMessageConverter, mockSmsFilter)

    @Test
    fun onReceiveWithNoMessages() {
        whenever(mockIntentToSmsMessageConverter.extractFrom(mockIntent)).thenReturn(emptyList())

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
        whenever(mockIntentToSmsMessageConverter.extractFrom(mockIntent)).thenReturn(listOf(mockSmsMessage))
        whenever(mockSmsFilter.shouldNotify(mockSmsMessage)).thenReturn(true)

        underTest.onReceive(mockContext, mockIntent)

        verify(mockNotifier).postNotification(notificationId, title, text)
        verifyZeroInteractions(mockFilteredOutSmsSaver)
    }

    @Test
    fun onReceiveNotNotifying() {
        whenever(mockIntentToSmsMessageConverter.extractFrom(mockIntent)).thenReturn(listOf(mockSmsMessage))
        whenever(mockSmsFilter.shouldNotify(mockSmsMessage)).thenReturn(false)

        underTest.onReceive(mockContext, mockIntent)

        verifyZeroInteractions(mockNotifier)
        verify(mockFilteredOutSmsSaver).saveFilteredOutSmsMessage(mockSmsMessage)
    }

    @Test
    fun createIdFromNormalNumber() {
        val number = "12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromMaxInt() {
        val number = Int.MAX_VALUE.toString()
        val expected = 147483647 // the last digits of Int max: 2 147483647

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromBiggerThanMaxInt() {
        val number = Long.MAX_VALUE.toString()
        val expected = 854775807 // the last digits of Long max: 9223372036 854775807

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromNumberContainingMinus() {
        val number = "-12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromNumberContainingPlus() {
        val number = "+12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromOnlyText() {
        val number = "only text, no numbers"
        whenever(mockSmsMessage.displayOriginatingAddress).thenReturn(number)

        val result = underTest.createIdFrom(number)

        result shouldBeInRange IntRange(Int.MIN_VALUE, Int.MAX_VALUE) // any random number
    }

    @Test
    fun createIdFromNumbersInSymbols() {
        val number = "+„¡“¶¢[]|{}≠¿'«∑€®†Ω¨øπ•±å‚∂ƒ©ªº∆@œ123æ‘≤¥≈ç√∫~∞…–°!§$%&/(\n)=?"
        val expected = 123

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun parameterLessConstructorExists() {
        SmsBroadcastReceiver(mockNotifier, mockFilteredOutSmsSaver) // TODO ASK how to test that it is possible to instantiate class without constructor parameters?
    }

}
