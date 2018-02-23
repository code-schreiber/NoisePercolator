package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.support.v4.app.NotificationCompat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.shouldEqual
import org.junit.Test

class NotificationCreatorTest {

    private val mockBuilder: NotificationCompat.Builder = mock()
    private val mockNotification: Notification = mock()

    private val underTest: NotificationCreator = NotificationCreator()

    @Test
    fun setNotificationAttributes() {
        val title = "title"
        val text = "text"
        whenever(mockBuilder.setSmallIcon(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentTitle(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentText(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.build()).thenReturn(mockNotification)

        val notification = underTest.setNotificationAttributes(mockBuilder, title, text)

        verify(mockBuilder).setSmallIcon(NotificationCreator.SMALL_ICON)
        verify(mockBuilder).setContentTitle(title)
        verify(mockBuilder).setContentText(text)

        notification shouldEqual mockNotification
    }

}
