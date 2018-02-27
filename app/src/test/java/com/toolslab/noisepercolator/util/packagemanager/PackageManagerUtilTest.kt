package com.toolslab.noisepercolator.util.packagemanager

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class PackageManagerUtilTest {

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockPackageManager: PackageManager = mock()
    private val mockApplicationInfo: ApplicationInfo = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: PackageManagerUtil = PackageManagerUtil(mockContext, mockSdkChecker)

    private val smsPackage = "an.sms.package"

    @Before
    fun setUp() {
        whenever(mockContext.packageManager).thenReturn(mockPackageManager)
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(listOf(dummyApplicationInfo(smsPackage)))
    }

    @Test
    fun launchDefaultSmsApp() {
        whenever(mockPackageManager.getLaunchIntentForPackage(smsPackage)).thenReturn(mockIntent)

        underTest.launchDefaultSmsApp()

        verify(mockContext).startActivity(mockIntent)
    }

    @Test
    fun launchDefaultSmsAppWithNullIntent() {
        whenever(mockPackageManager.getLaunchIntentForPackage(smsPackage)).thenReturn(null)

        underTest.launchDefaultSmsApp()

        verify(mockContext, never()).startActivity(any())
    }

    @Test
    fun getDefaultSmsAppName() {
        val label = "a label"
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(label)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual label
    }

    @Test
    fun getDefaultSmsAppNameWithNullLabel() {
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(null)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

    @Test
    fun getDefaultSmsAppNameWithNameNotFoundException() {
        whenever(mockPackageManager.getApplicationInfo(smsPackage, PackageManager.GET_META_DATA)).thenThrow(PackageManager.NameNotFoundException())

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

    @Test
    fun getDefaultSmsPackageLegacy() {
        val packages = listOf(dummyApplicationInfo("com.android.mms"),
                dummyApplicationInfo("com.example.sms.app"),
                dummyApplicationInfo("com.example.message.app"),
                dummyApplicationInfo("com.example.media.app"),
                dummyApplicationInfo("com.example.another.app"))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual "com.android.mms"
    }

    @Test
    fun getDefaultSmsPackageNoMessagingApp() {
        val packages = listOf(dummyApplicationInfo("com.example.another.app"))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual ""
    }

    private fun dummyApplicationInfo(packageName: String): ApplicationInfo {
        val applicationInfo = ApplicationInfo()
        applicationInfo.packageName = packageName
        return applicationInfo
    }

}
