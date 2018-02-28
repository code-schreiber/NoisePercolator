package com.toolslab.noisepercolator.view.messages

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class MessagesProviderTest {

    private val mockContext: Context = mock()
    private val mockContentResolver: ContentResolver = mock()
    private val mockUri: Uri = mock()
    private val mockCursor: Cursor = mock()
    private val mockMessage: Message = mock()
    private val mockSdkChecker: SdkChecker = mock()
    private val mockCursorToMessageConverter: CursorToMessageConverter = mock()

    val underTest = MessagesProvider(mockContext, mockSdkChecker, mockCursorToMessageConverter)

    @Before
    fun setup() {
        underTest.smsUri = mockUri
        whenever(mockContext.contentResolver).thenReturn(mockContentResolver)
        whenever(mockContentResolver.query(mockUri, null, null, null, null)).thenReturn(mockCursor)
        whenever(mockCursor.moveToFirst()).thenReturn(true)
        whenever(mockCursorToMessageConverter.convert(mockCursor)).thenReturn(mockMessage)
    }

    @Test
    fun getMessages() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(true)

        val result = underTest.getMessages()

        result.size shouldEqual 1
        result[0] shouldEqual mockMessage
    }

    @Test
    fun getMessagesLegacy() {
        whenever(mockSdkChecker.deviceIsKitkatOrAbove()).thenReturn(false)

        val result = underTest.getMessages()

        result.size shouldEqual 1
        result[0] shouldEqual mockMessage
    }

}
