package com.toolslab.noisepercolator.util

import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.*
import com.toolslab.noisepercolator.R
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
        verify(mockBaseActivity).onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithEmptyGrantResults() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual false
        verifyZeroInteractions(mockBaseActivity)
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithGrantedResult() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual true
        verifyZeroInteractions(mockBaseActivity)
    }

    @Test
    fun isOnRequestPermissionsResultGrantedWithDeniedResult() {
        val requestCode = PermissionsUtil.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)

        val result = underTest.isOnRequestPermissionsResultGranted(mockBaseActivity, requestCode, permissions, grantResults)

        result shouldEqual false
        verifyZeroInteractions(mockBaseActivity)
    }

    @Test
    fun showPermissionExplanation() {
        val resId = R.string.please_allow_sms_permission

        underTest.showPermissionExplanation(mockBaseActivity)

        verify(mockBaseActivity).showSimpleError(eq(resId), any())
    }

}
