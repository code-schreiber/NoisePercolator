package com.toolslab.noisepercolator.util.packagemanager

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.provider.Telephony
import android.support.annotation.VisibleForTesting
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber

class PackageManagerUtil(private val context: Context = NoisePercolator.applicationContext(),
                         private val sdkChecker: SdkChecker = SdkChecker()) {

    fun launchDefaultSmsApp() {
        val defaultSmsPackage = getDefaultSmsPackage()
        val intent = context.packageManager.getLaunchIntentForPackage(defaultSmsPackage)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            Timber.e("No launch intent for package $defaultSmsPackage")
//            openDefaultSmsPackageFallback()
        }
    }

    fun getDefaultSmsAppName(): String {
        val defaultSmsPackage = getDefaultSmsPackage()
        try {
            val applicationInfo = context.packageManager.getApplicationInfo(defaultSmsPackage, PackageManager.GET_META_DATA)
            val label = context.packageManager.getApplicationLabel(applicationInfo)
            if (label != null) {
                return label as String
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
        }
        Timber.e("No label for package $defaultSmsPackage")
        return ""
    }

    @VisibleForTesting
    fun getDefaultSmsPackage(): String {
        return if (sdkChecker.deviceIsKitkatOrAbove()) {
            getDefaultSmsPackageKitkat()
        } else {
            getDefaultSmsPackageLegacy()
        }
    }

    @VisibleForTesting
    @TargetApi(SdkChecker.KITKAT)
    fun getDefaultSmsPackageKitkat() = Telephony.Sms.getDefaultSmsPackage(context)

    private fun getDefaultSmsPackageLegacy(): String {
        val packageNames = mutableListOf<String>()
        val packages = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        packages
                .map { it.packageName }
                .filterTo(packageNames) {
                    it.contains("sms", true) ||
                            it.contains("mms", true) || // i.e. com.android.mms
                            it.contains("message", true) ||
                            it.contains("messaging", true) ||
                            it.contains("media", true) && it != "com.android.providers.media"
                }

        return when {
            packageNames.size < 1 -> {
                Timber.e("No messaging apps found")
//                getDefaultSmsPackageLegacyFallback()
                ""
            }
            packageNames.size == 1 -> {
                packageNames[0]
            }
            else -> {
                Timber.e("Taking first from more than one messaging app: $packageNames")
                packageNames[0]
            }
        }
    }

    // TODO test method on different devices
    private fun getDefaultSmsPackageLegacyFallback(): String {
        val name = "sms_default_application" // Taken from Settings.Secure.SMS_DEFAULT_APPLICATION
        val defaultSmsPackage = Settings.Secure.getString(context.contentResolver, name)
        return if (defaultSmsPackage == null) {
            Timber.e("getDefaultSmsPackageLegacyFallback failed to find defaultSmsPackage")
            ""
        } else {
            defaultSmsPackage
        }
    }

    // TODO test method on different devices
    private fun openDefaultSmsPackageFallback() {
        val type = "vnd.android-dir/mms-sms"
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = type
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}
