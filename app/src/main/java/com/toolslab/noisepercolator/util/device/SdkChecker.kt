package com.toolslab.noisepercolator.util.device

import android.os.Build

class SdkChecker {

    internal companion object {
        internal const val OREO = Build.VERSION_CODES.O
        internal const val NOUGAT = Build.VERSION_CODES.N
        internal const val KITKAT = Build.VERSION_CODES.KITKAT
    }

    fun deviceIsOreoOrAbove(): Boolean = deviceSdk() >= OREO

    fun deviceIsNougatOrAbove(): Boolean = deviceSdk() >= NOUGAT

    fun deviceIsKitkatOrAbove(): Boolean = deviceSdk() >= KITKAT

    private fun deviceSdk() = Build.VERSION.SDK_INT

}
