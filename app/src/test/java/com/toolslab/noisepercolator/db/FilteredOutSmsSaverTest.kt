package com.toolslab.noisepercolator.db

import android.telephony.SmsMessage
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test

class FilteredOutSmsSaverTest {

    private val mockSmsMessage: SmsMessage = mock()
    private val mockDataProvider: DataProvider = mock()

    private val underTest: FilteredOutSmsSaver = FilteredOutSmsSaver(mockDataProvider)

    @Test
    fun saveFilteredOutSmsMessage() {


        underTest.saveFilteredOutSmsMessage(mockSmsMessage)

        // TODO test after implementing saveFilteredOutSmsMessage()
    }

}
