package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class NotifierTest {

    private companion object {
        private const val NOTIFICATION_ID = 1
        private const val TITLE = "TITLE"
        private const val TEXT = "TEXT"
        private const val CHANNEL_ID = Notifier.CHANNEL_ID
    }

    private val mockContext: Context = mock()
    private val mockNotificationChannelCreator: NotificationChannelCreator = mock()
    private val mockNotificationManager: NotificationManager = mock()
    private val mockNotificationCreator: NotificationCreator = mock()
    private val mockNotification: Notification = mock()

    private val underTest: Notifier = Notifier(mockContext, mockNotificationChannelCreator, mockNotificationCreator)

    @Test
    fun postNotification() {
        whenever(mockContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mockNotificationManager)
        whenever(mockNotificationCreator.createNotification(mockContext, CHANNEL_ID, TITLE, TEXT)).thenReturn(mockNotification)

        underTest.postNotification(NOTIFICATION_ID, TITLE, TEXT)

        verify(mockNotificationChannelCreator).createNotificationChannel(mockNotificationManager, CHANNEL_ID)
        verify(mockNotificationManager).notify(NOTIFICATION_ID, mockNotification)
    }

}
