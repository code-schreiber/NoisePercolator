package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.messages.MessageConverter
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.inOrder

class DataProviderTest {

    companion object {
        private const val MESSAGE_AS_STRING = "a message"
        private val MESSAGE = Message("", 0L, "")
        private val MESSAGES = setOf(MESSAGE_AS_STRING)
    }

    private val mockSharedPreferences: SharedPreferences = mock()
    private val mockEditor: SharedPreferences.Editor = mock()
    private val mockContext: Context = mock()
    private val mockMessageConverter: MessageConverter = mock()

    private val underTest = DataProvider(mockContext, mockMessageConverter)

    @Before
    fun setUp() {
        whenever(mockContext.getSharedPreferences(DataProvider.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockMessageConverter.convert(MESSAGE)).thenReturn(MESSAGE_AS_STRING)
        whenever(mockMessageConverter.convert(MESSAGE_AS_STRING)).thenReturn(MESSAGE)
    }

    @Test
    fun getMessages() {
        whenever(mockSharedPreferences.getStringSet(DataProvider.MESSAGES_KEY, emptySet())).thenReturn(MESSAGES)

        val result = underTest.getMessages()

        result.size shouldEqual 1
        result[0] shouldEqual MESSAGE
    }

    @Test
    fun getMessagesDefault() {
        val result = underTest.getMessages()

        result shouldEqual emptyList()
    }

    @Test
    fun getMessagesStringSet() {
        whenever(mockSharedPreferences.getStringSet(DataProvider.MESSAGES_KEY, emptySet())).thenReturn(MESSAGES)

        val result = underTest.getMessagesStringSet()

        result shouldEqual MESSAGES
    }

    @Test
    fun getMessagesStringSetDefault() {
        val result = underTest.getMessagesStringSet()

        result shouldEqual emptySet()
    }

    @Test
    fun saveMessage() {
        underTest.saveMessage(MESSAGE)

        inOrder(mockEditor).apply {
            verify(mockEditor).putStringSet(DataProvider.MESSAGES_KEY, MESSAGES)
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
