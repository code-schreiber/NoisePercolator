package com.toolslab.noisepercolator.util.device

import android.os.Build

class SdkChecker {

    companion object {
        const val OREO = Build.VERSION_CODES.O
        internal const val KITKAT = Build.VERSION_CODES.KITKAT
    }

    fun deviceIsOreoOrAbove(): Boolean = deviceSdk() >= OREO

    fun deviceIsKitkatOrAbove(): Boolean = deviceSdk() >= KITKAT

    private fun deviceSdk() = Build.VERSION.SDK_INT

}
