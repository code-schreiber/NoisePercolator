package com.toolslab.noisepercolator.view.messages

import android.database.Cursor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Test
import java.text.DateFormat
import java.util.*

class CursorToMessageConverterTest {

    private companion object {
        private const val NUMBER_OF_COLUMNS = 3

        private const val INDEX_DATE = 0
        private const val INDEX_ADDRESS = 1
        private const val INDEX_BODY = 2

        private const val COLUMN_DATE = CursorToMessageConverter.COLUMN_DATE
        private const val COLUMN_ADDRESS = CursorToMessageConverter.COLUMN_ADDRESS
        private const val COLUMN_BODY = CursorToMessageConverter.COLUMN_BODY

        private const val ADDRESS = "an address"
        private const val DATE = "946681199000"
        private const val BODY = "a message body"
    }

    private val mockCursor: Cursor = mock()

    val underTest = CursorToMessageConverter()

    @Test
    fun convert() {
        val expectedFormattedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(DATE.toLong()))
        whenever(mockCursor.columnCount).thenReturn(NUMBER_OF_COLUMNS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_ADDRESS)).thenReturn(INDEX_ADDRESS)
        whenever(mockCursor.getString(INDEX_ADDRESS)).thenReturn(ADDRESS)
        whenever(mockCursor.getColumnName(INDEX_ADDRESS)).thenReturn(COLUMN_ADDRESS)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_DATE)).thenReturn(INDEX_DATE)
        whenever(mockCursor.getString(INDEX_DATE)).thenReturn(DATE)
        whenever(mockCursor.getColumnName(INDEX_DATE)).thenReturn(COLUMN_DATE)
        whenever(mockCursor.getColumnIndexOrThrow(COLUMN_BODY)).thenReturn(INDEX_BODY)
        whenever(mockCursor.getString(INDEX_BODY)).thenReturn(BODY)
        whenever(mockCursor.getColumnName(INDEX_BODY)).thenReturn(COLUMN_BODY)

        val result = underTest.convert(mockCursor)

        result.apply {
            address shouldEqual ADDRESS
            date shouldEqual expectedFormattedDate
            body shouldEqual BODY
        }
    }

}
