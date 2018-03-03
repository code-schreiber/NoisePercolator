package com.toolslab.noisepercolator.db

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.messages.MessageConverter
import org.junit.Test
import org.mockito.Mockito.verify

class FilteredOutSmsSaverTest {

    private val mockSmsMessage: SmsMessage = mock()
    private val mockMessage: Message = mock()
    private val mockDataProvider: DataProvider = mock()
    private val mockMessageConverter: MessageConverter = mock()

    private val underTest: FilteredOutSmsSaver = FilteredOutSmsSaver(mockDataProvider, mockMessageConverter)

    @Test
    fun saveFilteredOutSmsMessage() {
        whenever(mockMessageConverter.convert(mockSmsMessage)).thenReturn(mockMessage)

        underTest.saveFilteredOutSmsMessage(mockSmsMessage)

        verify(mockDataProvider).saveMessage(mockMessage)
    }

}
