package com.toolslab.noisepercolator.view

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Test

class MessagesProviderTest {

    private companion object {
        private const val NUMBER_OF_COLUMNS = 3

        private const val INDEX_DATE = 0
        private const val INDEX_ADDRESS = 1
        private const val INDEX_BODY = 2

        private const val COLUMN_DATE = "date"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_BODY = "body"

        private const val DATE = "a date"
        private const val ADDRESS = "an address"
        private const val BODY = "a message body"
    }

    private val mockContentResolver: ContentResolver = mock()
    private val mockUri: Uri = mock()
    private val mockCursor: Cursor = mock()
    private val mockSdkChecker: SdkChecker = mock()

    val underTest = MessagesProvider(mockSdkChecker)

    @Test
    fun getMessages() {
        underTest.smsUri = mockUri
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(true)
        whenever(mockContentResolver.query(mockUri, null, null, null, null)).thenReturn(mockCursor)
        whenever(mockCursor.moveToFirst()).thenReturn(true)
        whenever(mockCursor.columnCount).thenReturn(NUMBER_OF_COLUMNS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_DATE)).thenReturn(INDEX_DATE)
        whenever(mockCursor.getString(INDEX_DATE)).thenReturn(DATE)
        whenever(mockCursor.getColumnName(INDEX_DATE)).thenReturn(COLUMN_DATE)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_ADDRESS)).thenReturn(INDEX_ADDRESS)
        whenever(mockCursor.getString(INDEX_ADDRESS)).thenReturn(ADDRESS)
        whenever(mockCursor.getColumnName(INDEX_ADDRESS)).thenReturn(COLUMN_ADDRESS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_BODY)).thenReturn(INDEX_BODY)
        whenever(mockCursor.getString(INDEX_BODY)).thenReturn(BODY)
        whenever(mockCursor.getColumnName(INDEX_BODY)).thenReturn(COLUMN_BODY)

        val result = underTest.getMessages(mockContentResolver)

        result.size shouldEqual 1
        result[0].apply {
            date shouldEqual DATE
            address shouldEqual ADDRESS
            body shouldEqual BODY
            debugInfo shouldEqual "date: a date; address: an address; body: a message body; "
        }
    }

    @Test
    fun getMessagesLegacy() {
        underTest.smsUri = mockUri
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)
        whenever(mockContentResolver.query(mockUri, null, null, null, null)).thenReturn(mockCursor)
        whenever(mockCursor.moveToFirst()).thenReturn(true)
        whenever(mockCursor.columnCount).thenReturn(NUMBER_OF_COLUMNS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_DATE)).thenReturn(INDEX_DATE)
        whenever(mockCursor.getString(INDEX_DATE)).thenReturn(DATE)
        whenever(mockCursor.getColumnName(INDEX_DATE)).thenReturn(COLUMN_DATE)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_ADDRESS)).thenReturn(INDEX_ADDRESS)
        whenever(mockCursor.getString(INDEX_ADDRESS)).thenReturn(ADDRESS)
        whenever(mockCursor.getColumnName(INDEX_ADDRESS)).thenReturn(COLUMN_ADDRESS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_BODY)).thenReturn(INDEX_BODY)
        whenever(mockCursor.getString(INDEX_BODY)).thenReturn(BODY)
        whenever(mockCursor.getColumnName(INDEX_BODY)).thenReturn(COLUMN_BODY)

        val result = underTest.getMessages(mockContentResolver)

        result.size shouldEqual 1
        result[0].apply {
            date shouldEqual DATE
            address shouldEqual ADDRESS
            body shouldEqual BODY
            debugInfo shouldEqual "date: a date; address: an address; body: a message body; "
        }
    }

}
