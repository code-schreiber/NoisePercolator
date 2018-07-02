package com.toolslab.noisepercolator.util.packagemanager

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.util.device.SdkChecker
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Test

class PackageManagerUtilTest {

    companion object {
        private const val SMS_PACKAGE = "a.sms.package"
        private const val SMS_APP_GOOGLE_MESSAGING = "com.google.android.apps.messaging"
        private const val SMS_APP_ANDROID_MESSAGING = "com.android.messaging"
        private const val SMS_APP_MMS = "com.android.mms"
    }

    private val mockContext: Context = mock()
    private val mockIntent: Intent = mock()
    private val mockPackageManager: PackageManager = mock()
    private val mockApplicationInfo: ApplicationInfo = mock()
    private val mockSdkChecker: SdkChecker = mock()

    private val underTest: PackageManagerUtil = PackageManagerUtil(mockContext, mockSdkChecker)

    @Before
    fun setUp() {
        whenever(mockContext.packageManager)
                .thenReturn(mockPackageManager)
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA))
                .thenReturn(listOf(applicationInfo(SMS_PACKAGE)))
    }

    @Test
    fun launchDefaultSmsApp() {
        whenever(mockPackageManager.getLaunchIntentForPackage(SMS_PACKAGE)).thenReturn(mockIntent)

        underTest.launchDefaultSmsApp()

        verify(mockIntent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verify(mockContext).startActivity(mockIntent)
    }

    @Test
    fun createDefaultSmsAppFallbackIntent() {
        underTest.createDefaultSmsAppFallbackIntent(mockIntent)

        verify(mockIntent).action = Intent.ACTION_MAIN
        verify(mockIntent).addCategory(Intent.CATEGORY_DEFAULT)
        verify(mockIntent).type = "vnd.android-dir/mms-sms"
        verify(mockIntent).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        verifyNoMoreInteractions(mockIntent)
    }

    @Test
    fun getDefaultSmsAppName() {
        val label = "a label"
        whenever(mockPackageManager.getApplicationInfo(SMS_PACKAGE, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(label)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual label
    }

    @Test
    fun getDefaultSmsAppNameWithNullLabel() {
        whenever(mockPackageManager.getApplicationInfo(SMS_PACKAGE, PackageManager.GET_META_DATA)).thenReturn(mockApplicationInfo)
        whenever(mockPackageManager.getApplicationLabel(mockApplicationInfo)).thenReturn(null)

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

    @Test
    fun getDefaultSmsAppNameWithNameNotFoundException() {
        whenever(mockPackageManager.getApplicationInfo(SMS_PACKAGE, PackageManager.GET_META_DATA))
                .thenThrow(PackageManager.NameNotFoundException())

        val result = underTest.getDefaultSmsAppName()

        result shouldEqual ""
    }

    @Test
    fun getDefaultSmsPackageLegacyForAndroidAppsMessaging() {
        val packages = listOf(applicationInfo(SMS_APP_GOOGLE_MESSAGING))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_GOOGLE_MESSAGING
    }

    @Test
    fun getDefaultSmsPackageLegacyForAndroidMessaging() {
        val packages = listOf(applicationInfo(SMS_APP_ANDROID_MESSAGING))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_ANDROID_MESSAGING
    }

    @Test
    fun getDefaultSmsPackageLegacyForAndroidMms() {
        val packages = listOf(applicationInfo(SMS_APP_MMS))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_MMS
    }

    @Test
    fun getDefaultSmsPackageLegacyPrefersGoogleMessaging() {
        val packages = listOf(applicationInfo(SMS_APP_MMS),
                applicationInfo(SMS_APP_ANDROID_MESSAGING),
                applicationInfo(SMS_APP_GOOGLE_MESSAGING))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_GOOGLE_MESSAGING
    }

    @Test
    fun getDefaultSmsPackageLegacyPrefersGoogleMessagingOverMms() {
        val packages = listOf(applicationInfo(SMS_APP_MMS),
                applicationInfo(SMS_APP_GOOGLE_MESSAGING))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_GOOGLE_MESSAGING
    }

    @Test
    fun getDefaultSmsPackageLegacyPrefersGoogleMessagingOverAndroidMessaging() {
        val packages = listOf(applicationInfo(SMS_APP_GOOGLE_MESSAGING),
                applicationInfo(SMS_APP_ANDROID_MESSAGING))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual SMS_APP_GOOGLE_MESSAGING
    }

    @Test
    fun getDefaultSmsPackageForInvalidMessagingApps() {
        val packages = listOf(applicationInfo("com.android.providers.media"),
                applicationInfo("com.android.smspush"),
                applicationInfo("com.sec.android.nearby.mediaserver"),
                applicationInfo("com.example.another.app"))
        whenever(mockPackageManager.getInstalledApplications(PackageManager.GET_META_DATA)).thenReturn(packages)

        val result = underTest.getDefaultSmsPackage()

        result shouldEqual ""
    }

    private fun applicationInfo(packageName: String): ApplicationInfo {
        val applicationInfo = ApplicationInfo()
        applicationInfo.packageName = packageName
        return applicationInfo
    }

}
