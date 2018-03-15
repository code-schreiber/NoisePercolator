package com.toolslab.noisepercolator.view.base


import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

import com.toolslab.noisepercolator.view.common.DialogFactory
import timber.log.Timber

@SuppressLint("Registered") // BaseActivity should not go in the manifest
open class BaseActivity(private val dialogFactory: DialogFactory = DialogFactory()) : AppCompatActivity() {

    fun showSimpleDialog(message: String) {
        Timber.d("Showing dialog with message $message")
        dialogFactory.withMessage(this, message)
    }

    fun showSimpleError(@StringRes resId: Int, onOkAction: () -> Unit) {
        showSimpleError(getString(resId), onOkAction)
    }

    fun showSimpleError(message: String, onOkAction: () -> Unit) {
        Timber.e("Showing error with message $message")
        dialogFactory.withMessage(this, message, onOkAction)
    }

}
