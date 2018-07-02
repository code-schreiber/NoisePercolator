package com.toolslab.noisepercolator.util.packagemanager

import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.support.annotation.VisibleForTesting
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber

class PackageManagerUtil(private val context: Context = NoisePercolator.applicationContext(),
                         private val sdkChecker: SdkChecker = SdkChecker()) {

    companion object {

        @VisibleForTesting
        const val PACKAGE_NAME_SMS = "sms"

        @VisibleForTesting
        const val PACKAGE_NAME_SMS_PUSH = "com.android.smspush"

        @VisibleForTesting
        const val PACKAGE_NAME_MMS = "mms"

        @VisibleForTesting
        const val PACKAGE_NAME_MESSAGE = "message"

        @VisibleForTesting
        const val PACKAGE_NAME_MESSAGING = "messaging"

        @VisibleForTesting
        const val PACKAGE_NAME_MEDIA = "media"

        @VisibleForTesting
        const val PACKAGE_NAME_MEDIA_PROVIDER = "com.android.providers.media"

        @VisibleForTesting
        const val PACKAGE_NAME_MEDIA_SERVER = "com.sec.android.nearby.mediaserver"

        @VisibleForTesting
        const val PACKAGE_NAME_GOOGLE = "google"
    }

    fun launchDefaultSmsApp() {
        val intent = createLaunchDefaultSmsAppIntent()
        context.startActivity(intent)
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

    fun createLaunchDefaultSmsAppPendingIntent(): PendingIntent {
        val intent = createLaunchDefaultSmsAppIntent()
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun createLaunchDefaultSmsAppIntent(): Intent {
        val defaultSmsPackage = getDefaultSmsPackage()
        val intent = context.packageManager.getLaunchIntentForPackage(defaultSmsPackage)
        return if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            Timber.e("No launch intent for package $defaultSmsPackage, going for fallback")
            createDefaultSmsAppFallbackIntent(Intent())
        }
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
    fun getDefaultSmsPackageKitkat(): String {
        return Telephony.Sms.getDefaultSmsPackage(context) ?: ""
    }

    private fun getDefaultSmsPackageLegacy(): String {
        val packageNames = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .map { it.packageName }
                .filter {
                    isSmsApp(it) ||
                            isMmsApp(it) ||
                            isMessageApp(it) ||
                            isMessagingApp(it) ||
                            isMediaApp(it)
                }

        return when {
            packageNames.isEmpty() -> {
                Timber.e("No messaging apps found")
                ""
            }
            packageNames.size == 1 -> {
                packageNames[0]
            }
            else -> {
                getPreferredPackageName(packageNames)
            }
        }
    }

    private fun getPreferredPackageName(packageNames: List<String>): String {
        val messagingApps = packageNames.filter { isMessagingApp(it) }
        if (messagingApps.size == 1) {
            return messagingApps[0]
        } else if (messagingApps.size > 1) {
            val googleApps = messagingApps.filter { isGoogleApp(it) }
            if (googleApps.isNotEmpty()) {
                // Not expecting more than one result, just return first one
                return googleApps[0]
            }
        }
        Timber.e("Taking first from more than one messaging app: $packageNames")
        return packageNames[0]
    }

    private fun isSmsApp(packageName: String) = packageName.contains(PACKAGE_NAME_SMS) && packageName != PACKAGE_NAME_SMS_PUSH

    private fun isMmsApp(packageName: String) = packageName.contains(PACKAGE_NAME_MMS)

    private fun isMessageApp(packageName: String) = packageName.contains(PACKAGE_NAME_MESSAGE)

    private fun isMessagingApp(packageName: String) = packageName.contains(PACKAGE_NAME_MESSAGING)

    private fun isMediaApp(packageName: String) = packageName.contains(PACKAGE_NAME_MEDIA) && packageName != PACKAGE_NAME_MEDIA_PROVIDER && packageName != PACKAGE_NAME_MEDIA_SERVER

    private fun isGoogleApp(it: String) = it.contains(PACKAGE_NAME_GOOGLE)

    @VisibleForTesting
    fun createDefaultSmsAppFallbackIntent(intent: Intent): Intent {
        val type = "vnd.android-dir/mms-sms"
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = type
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

}
