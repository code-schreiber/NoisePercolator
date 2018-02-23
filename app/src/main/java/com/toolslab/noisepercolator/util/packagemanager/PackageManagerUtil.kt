package com.toolslab.noisepercolator.util.packagemanager

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.receiver.IntentToSmsMessageConverter
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber

@TargetApi(SdkChecker.KITKAT)
class PackageManagerUtil(private val context: Context = NoisePercolator.applicationContext(), private val intentToSmsMessageConverter: IntentToSmsMessageConverter = IntentToSmsMessageConverter()) {

    fun launchDefaultSmsApp() {
        val defaultSmsPackage = intentToSmsMessageConverter.getDefaultSmsPackage(context)
        val intent = context.packageManager.getLaunchIntentForPackage(defaultSmsPackage)
        if (intent != null) {
            context.startActivity(intent)
        } else {
            Timber.e("No launch intent for $defaultSmsPackage")
        }
    }

    fun getDefaultSmsAppName(): String {
        val defaultSmsPackage = intentToSmsMessageConverter.getDefaultSmsPackage(context)
        try {
            val applicationInfo = context.packageManager.getApplicationInfo(defaultSmsPackage, PackageManager.GET_META_DATA)
            val label = context.packageManager.getApplicationLabel(applicationInfo)
            if (label != null) {
                return label as String
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
        }
        Timber.e("No label for $defaultSmsPackage")
        return ""
    }

}