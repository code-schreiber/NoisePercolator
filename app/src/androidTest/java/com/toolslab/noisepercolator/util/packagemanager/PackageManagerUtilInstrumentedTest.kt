package com.toolslab.noisepercolator.util.packagemanager

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PackageManagerUtilInstrumentedTest {

    private val underTest = PackageManagerUtil(InstrumentationRegistry.getTargetContext())

    @Test
    fun getDefaultSmsPackageKitkat() {
        val expectedSmsPackage = "com.android.messaging"

        val result = underTest.getDefaultSmsPackageKitkat()

        assertEquals(expectedSmsPackage, result)
    }

}
