package com.toolslab.noisepercolator.util

import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.mock
import com.toolslab.noisepercolator.view.base.BaseActivity
import org.amshove.kluent.shouldEqual
import org.junit.Test

class PermissionsUtilTest {

    private val mockBaseActivity: BaseActivity = mock()

    private val underTest = PermissionsUtil()

    @Test
    fun isOnRequestPermissionsResultGrantedWithWrongRequestCode() {
        val requestCode = -1
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual false
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithEmptyGrantResults() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST

        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual false
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithGrantedResult() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST

        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual true
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithDeniedResult() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST

        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual false
    }

}
