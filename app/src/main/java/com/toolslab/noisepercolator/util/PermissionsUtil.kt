package com.toolslab.noisepercolator.util

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.toolslab.noisepercolator.view.base.BaseActivity
import timber.log.Timber

// TODO test class
class PermissionsUtil {

    companion object {
        private const val READ_SMS_PERMISSIONS_REQUEST = 100
    }

    internal fun isOnRequestPermissionsResultGranted(activity: BaseActivity, requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                return true
            }
        } else {
            Timber.e("Unknown requestCode: $requestCode")
            activity.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        return false
    }

    internal fun maybeShowPermissionExplanation(activity: BaseActivity) {
        if (noPermission(activity)) {
            if (shouldShowRequestPermission(activity)) {
                showPermissionExplanation(activity)
            } else {
                // No explanation needed, we can request the permission.
                requestPermission(activity)
            }
        }
    }

    internal fun noPermission(activity: BaseActivity) =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

    private fun showPermissionExplanation(activity: BaseActivity) =
            activity.showSimpleError("Please allow permission after clicking ok", { requestPermission(activity) })

    private fun shouldShowRequestPermission(activity: BaseActivity) =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)

    private fun requestPermission(activity: BaseActivity) =
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_SMS), READ_SMS_PERMISSIONS_REQUEST)

}
