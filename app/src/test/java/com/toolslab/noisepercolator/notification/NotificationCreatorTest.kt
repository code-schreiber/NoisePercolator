package com.toolslab.noisepercolator.notification

import android.app.Notification
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
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
    private val mockPendingIntent: PendingIntent = mock()
    private val mockSdkChecker: SdkChecker = mock()
    private val mockPackageManagerUtil: PackageManagerUtil = mock()

    private val underTest: NotificationCreator = NotificationCreator(mockSdkChecker, mockPackageManagerUtil)

    @Before
    fun setUp() {
        whenever(mockPackageManagerUtil.createLaunchDefaultSmsAppPendingIntent()).thenReturn(mockPendingIntent)
        whenever(mockBuilder.setSmallIcon(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentTitle(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentText(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setContentIntent(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setAutoCancel(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setCategory(any())).thenReturn(mockBuilder)
        whenever(mockBuilder.build()).thenReturn(mockNotification)
    }

    @Test
    fun setNotificationAttributes() {
        whenever(mockSdkChecker.deviceIsNougatOrAbove()).thenReturn(true)

        val notification = underTest.setNotificationAttributes(mockBuilder, TITLE, TEXT)

        verify(mockBuilder).setSmallIcon(NotificationCreator.SMALL_ICON)
        verify(mockBuilder).setContentTitle(TITLE)
        verify(mockBuilder).setContentText(TEXT)
        verify(mockBuilder).setContentIntent(mockPendingIntent)
        verify(mockBuilder).setAutoCancel(true)
        verify(mockBuilder).setCategory(NotificationCompat.CATEGORY_MESSAGE)

        notification shouldEqual mockNotification
    }

    @Test
    fun setNotificationSmallIconLegacy() {
        whenever(mockSdkChecker.deviceIsNougatOrAbove()).thenReturn(false)

        underTest.setNotificationAttributes(mockBuilder, TITLE, TEXT)

        verify(mockBuilder).setSmallIcon(NotificationCreator.SMALL_ICON_LEGACY)
    }

}
