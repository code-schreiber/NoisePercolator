package com.toolslab.noisepercolator.util.packagemanager

import android.content.Context
import android.content.pm.PackageManager
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.receiver.SmsMessagesConverter
import timber.log.Timber

class PackageManagerUtil(private val context: Context = NoisePercolator.applicationContext(),
                         private val smsMessagesConverter: SmsMessagesConverter = SmsMessagesConverter()) {

    fun launchDefaultSmsApp() {
        val defaultSmsPackage = smsMessagesConverter.getDefaultSmsPackage(context)
        val intent = context.packageManager.getLaunchIntentForPackage(defaultSmsPackage)
        if (intent != null) {
            context.startActivity(intent)
        } else {
            Timber.e("No launch intent for package $defaultSmsPackage")
        }
    }

    fun getDefaultSmsAppName(): String {
        val defaultSmsPackage = smsMessagesConverter.getDefaultSmsPackage(context)
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

}
