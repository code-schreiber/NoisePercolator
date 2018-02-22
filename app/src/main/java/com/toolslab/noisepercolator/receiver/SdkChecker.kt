package com.toolslab.noisepercolator.receiver

import android.os.Build

class SdkChecker {

    fun deviceIsKitkatOrAbove(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

}
