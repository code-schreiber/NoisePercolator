package com.toolslab.noisepercolator.util.packagemanager

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.receiver.IntentToSmsMessageConverter
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.mockito.Mockito.*

class PackageManagerUtilTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockPackageManager: PackageManager = mock()
    private val mockApplicationInfo: ApplicationInfo = mock()
    private val mockIntentToSmsMessageConverter: IntentToSmsMessageConverter = mock()

    private val underTest: PackageManagerUtil = PackageManagerUtil(mockContext, mockIntentToSmsMessageConverter)

    private val smsPackage = "a.package"

    @Test
    fun launchDefaultSmsApp() {
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockIntentToSmsMessageConverter.getDefaultSmsPackage(mockContext)).thenReturn(smsPackage)
        whenever(mockPackageManager.getLaunchIntentForPackage(smsPackage)).thenReturn(mockIntent)

        underTest.launchDefaultSmsApp()

        verify(mockContext).startActivity(mockIntent)
    }

    @Test
    fun launchDefaultSmsAppWithNullIntent() {
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockIntentToSmsMessageConverter.getDefaultSmsPackage(mockContext)).thenReturn(smsPackage)
        whenever(mockPackageManager.getLaunchIntentForPackage(smsPackage)).thenReturn(null)

        underTest.launchDefaultSmsApp()

        verify(mockContext, never()).startActivity(any())
    }

    @Test
    fun getDefaultSmsAppName() {
        val label = "a label"
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockIntentToSmsMessageConverter.getDefaultSmsPackage(mockContext)).thenReturn(smsPackage)
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(label)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual label
    }

    @Test
    fun getDefaultSmsAppNameWithNullLabel() {
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(null)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

    @Test
    fun getDefaultSmsAppNameWithNameNotFoundException() {
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenThrow(PackageManager.NameNotFoundException())

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

}
