package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.support.v4.app.NotificationCompat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class NotificationCreatorTest {

    companion object {
        const val TITLE = "a title"
        const val TEXT = "a text"
    }

    private val mockBuilder: NotificationCompat.Builder = mock()
    private val mockNotification: Notification = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: NotificationCreator = NotificationCreator(mockSdkChecker)

    @Before
    fun setUp() {
        whenever(mockBuilder.setSmallIcon(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentTitle(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentText(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.build()).thenReturn(mockNotification)
    }

    @Test
    fun setNotificationAttributes() {
        whenever(mockSdkChecker.deviceIsNougatOrAbove()).thenReturn(true)

        val notification = underTest.setNotificationAttributes(mockBuilder, TITLE, TEXT)

        verify(mockBuilder).setSmallIcon(NotificationCreator.SMALL_ICON)
        verify(mockBuilder).setContentTitle(TITLE)
        verify(mockBuilder).setContentText(TEXT)

        notification shouldEqual mockNotification
    }

    @Test
    fun setNotificationAttributesLegacy() {
        whenever(mockSdkChecker.deviceIsNougatOrAbove()).thenReturn(false)

        val notification = underTest.setNotificationAttributes(mockBuilder, TITLE, TEXT)

        verify(mockBuilder).setSmallIcon(NotificationCreator.SMALL_ICON_LEGACY)
        verify(mockBuilder).setContentTitle(TITLE)
        verify(mockBuilder).setContentText(TEXT)

        notification shouldEqual mockNotification
    }

}
