package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.inOrder

class DataProviderTest {

    companion object {
        private const val A_NUMBER = 123
    }

    private val mockContext: Context = mock()
    private val mockSharedPreferences: SharedPreferences = mock()
    private val mockEditor: SharedPreferences.Editor = mock()

    private val underTest = DataProvider(mockContext)

    @Before
    fun setUp() {
        whenever(mockContext.getSharedPreferences(DataProvider.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
    }

    @Test
    fun getNumberOfMessages() {
        whenever(mockSharedPreferences.getInt(DataProvider.NUMBER_OF_MESSAGES_KEY, 0)).thenReturn(Companion.A_NUMBER)

        val result = underTest.getNumberOfMessages()

        result shouldEqual A_NUMBER
    }

    @Test
    fun getNumberOfMessagesDefault() {
        val result = underTest.getNumberOfMessages()

        result shouldEqual 0
    }

    @Test
    fun setNumberOfMessages() {
        underTest.setNumberOfMessages(A_NUMBER)

        inOrder(mockEditor).apply {
            verify(mockEditor).putInt(DataProvider.NUMBER_OF_MESSAGES_KEY, A_NUMBER)
            verify(mockEditor).apply()
        }
    }

    @Test
    fun clearPreferences() {
        underTest.clearPreferences()

        inOrder(mockEditor).apply {
            verify(mockEditor).clear()
            verify(mockEditor).apply()
        }
    }

}
