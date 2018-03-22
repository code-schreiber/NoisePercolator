package com.toolslab.noisepercolator.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.junit.Test

class NotificationChannelCreatorTest {

    private val mockSdkChecker: SdkChecker = mock()
    private val mockNotificationManager: NotificationManager = mock()
    private val mockNotificationChannel: NotificationChannel = mock()

    private val underTest: NotificationChannelCreator = NotificationChannelCreator(mockSdkChecker)

    @Test
    fun createNotificationChannelBelowOreo() {
        whenever(mockSdkChecker.deviceIsOreoOrAbove()).thenReturn(false)

        underTest.createNotificationChannel(mockNotificationManager, "")

        verifyZeroInteractions(mockNotificationManager)
    }

    @Test
    fun createChannel() {
        underTest.createChannel(mockNotificationChannel, mockNotificationManager)

        val inOrder = inOrder(mockNotificationChannel, mockNotificationManager)
        inOrder.apply {
            verify(mockNotificationChannel).description = NotificationChannelCreator.CHANNEL_DESCRIPTION
            verify(mockNotificationManager).createNotificationChannel(mockNotificationChannel)
        }
    }

}
