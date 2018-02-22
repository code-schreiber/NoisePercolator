package com.toolslab.noisepercolator.util.packagemanager

import android.content.Context
import android.content.pm.PackageManager
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.receiver.IntentToSmsMessageConverter

class OtherAppHandler(private val context: Context = NoisePercolator.applicationContext(), intentToSmsMessageConverter: IntentToSmsMessageConverter = IntentToSmsMessageConverter()) {

    private val packageManager = context.packageManager
    private val defaultSmsPackage = intentToSmsMessageConverter.getDefaultSmsPackage(context)

    fun launchDefaultSmsApp() {
        val intent = packageManager.getLaunchIntentForPackage(defaultSmsPackage) // TODO could be null
        context.startActivity(intent)
    }

    fun getDefaultSmsAppName(): String {
        return context.packageManager.getApplicationLabel(packageManager.getApplicationInfo(defaultSmsPackage, PackageManager.GET_META_DATA)) as String // TODO ASK make string null safe in kotlin
    }

}
