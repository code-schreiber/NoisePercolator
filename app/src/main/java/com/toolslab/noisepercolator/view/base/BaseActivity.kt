package com.toolslab.noisepercolator.view.base


import android.annotation.SuppressLint
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity

import com.toolslab.noisepercolator.view.common.ShowDialog
import timber.log.Timber


@SuppressLint("Registered") // BaseActivity should not go in the manifest
open class BaseActivity : AppCompatActivity() {

    fun showSimpleDialog(@StringRes resId: Int) {
        showSimpleDialog(getString(resId))
    }

    protected fun showSimpleDialog(message: String) {
        Timber.d("Showing dialog with message $message")
        ShowDialog().withMessage(this, message)
    }

    protected fun showSimpleError(@StringRes resId: Int, vararg formatArgs: Any) {
        showSimpleError(getString(resId, *formatArgs))
    }

    protected fun showSimpleError(message: String) {
        showSimpleError(message, {})
    }

    fun showSimpleError(message: String, onOkAction: () -> Unit) {
        Timber.e("Showing error with message $message")
        ShowDialog().withMessageAndOk(this, message, onOkAction)
    }

}
