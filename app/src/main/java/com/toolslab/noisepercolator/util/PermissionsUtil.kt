package com.toolslab.noisepercolator.util

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.view.base.BaseActivity
import timber.log.Timber

class PermissionsUtil {

    companion object {

        @VisibleForTesting
        const val READ_SMS_PERMISSIONS_REQUEST = 100

        private const val READ_SMS_PERMISSION = Manifest.permission.READ_SMS
    }

    internal fun isOnRequestPermissionsResultGranted(activity: BaseActivity,
                                                     requestCode: Int,
                                                     permissions: Array<String>,
                                                     grantResults: IntArray): Boolean {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                return true
            }
        } else {
            Timber.e("Unknown requestCode: $requestCode")
            activity.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        return false
    }

    internal fun maybeShowPermissionExplanation(activity: BaseActivity) {
        if (shouldShowRequestPermission(activity)) {
            showPermissionExplanation(activity)
        } else {
            // No explanation needed, we can request the permission.
            requestPermission(activity)
        }
    }

    @CheckResult
    internal fun hasSmsPermission(activity: BaseActivity): Boolean =
            ContextCompat.checkSelfPermission(activity, READ_SMS_PERMISSION) == PERMISSION_GRANTED

    private fun showPermissionExplanation(activity: BaseActivity) {
        activity.showSimpleError(R.string.Please_allow_permission_after_clicking_ok, { requestPermission(activity) })
    }

    @CheckResult
    private fun shouldShowRequestPermission(activity: BaseActivity): Boolean =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, READ_SMS_PERMISSION)

    private fun requestPermission(activity: BaseActivity) {
        ActivityCompat.requestPermissions(activity, arrayOf(READ_SMS_PERMISSION), READ_SMS_PERMISSIONS_REQUEST)
    }

}
