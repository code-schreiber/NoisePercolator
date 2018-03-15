package com.toolslab.noisepercolator.util.packagemanager

import android.os.Build.VERSION_CODES.*
import android.support.test.InstrumentationRegistry
import android.support.test.filters.SdkSuppress
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PackageManagerUtilInstrumentedTest {

    private val underTest = PackageManagerUtil(InstrumentationRegistry.getTargetContext())

    @SdkSuppress(minSdkVersion = N)
    @Test
    fun getDefaultSmsPackageForNougatAndUp() {
        val expectedSmsPackage = "com.google.android.apps.messaging"

        val result = underTest.getDefaultSmsPackageKitkat()

        assertEquals(expectedSmsPackage, result)
    }

    @SdkSuppress(minSdkVersion = KITKAT_WATCH, maxSdkVersion = M)
    @Test
    fun getDefaultSmsPackageFromKitkatToMarshmallow() {
        val expectedSmsPackage = "com.android.messaging"

        val result = underTest.getDefaultSmsPackageKitkat()

        assertEquals(expectedSmsPackage, result)
    }

    @SdkSuppress(maxSdkVersion = LOLLIPOP_MR1)
    @Test
    fun getDefaultSmsPackageOlderVersions() {
        val expectedSmsPackage = "com.android.mms"

        val result = underTest.getDefaultSmsPackageKitkat()

        assertEquals(expectedSmsPackage, result)
    }

}
