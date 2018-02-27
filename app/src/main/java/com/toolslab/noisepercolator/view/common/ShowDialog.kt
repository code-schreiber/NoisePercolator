package com.toolslab.noisepercolator.view.common


import android.app.AlertDialog
import android.content.Context


class ShowDialog {

    fun withMessage(context: Context, message: String) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .create()
                .show()
    }

    fun withMessageAndOk(context: Context, message: String, onOkAction: () -> Unit) {
        AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", { _, _ -> onOkAction() })
                .create()
                .show()
    }

}
