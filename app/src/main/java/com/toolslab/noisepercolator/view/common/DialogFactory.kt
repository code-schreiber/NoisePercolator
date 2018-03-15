package com.toolslab.noisepercolator.view.common


import android.app.AlertDialog
import android.content.Context
import com.toolslab.noisepercolator.R

class DialogFactory {

    fun withMessage(context: Context, message: String) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .create()
                .show()
    }

    fun withMessage(context: Context, message: String, onOkAction: () -> Unit) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, { _, _ -> onOkAction() })
                .create()
                .show()
    }

}
