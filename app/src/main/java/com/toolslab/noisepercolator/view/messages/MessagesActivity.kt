package com.toolslab.noisepercolator.view.messages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_messages.*
import timber.log.Timber


class MessagesActivity(private val presenter: MessagesContract.Presenter = MessagesPresenter())
    : BaseActivity(), MessagesContract.View {

    companion object {

        @VisibleForTesting
        const val READ_SMS_PERMISSIONS_REQUEST = 100

        @VisibleForTesting
        const val READ_SMS_PERMISSION = Manifest.permission.READ_SMS
    }

    @VisibleForTesting
    lateinit var defaultSmsAppButton: Button

    @VisibleForTesting
    lateinit var infoText: TextView

    @VisibleForTesting
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        initViews()
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.unbind(this)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.smsPermissionsGranted()
                return
            }
        } else {
            Timber.e("Unknown requestCode: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        presenter.onNoPermission()
    }

    override fun setDefaultSmsAppButtonText(defaultSmsAppName: String) {
        defaultSmsAppButton.text = getString(R.string.open_default_sms_app, defaultSmsAppName)
    }

    override fun setDefaultSmsAppButtonTextFallback() {
        defaultSmsAppButton.setText(R.string.open_sms_app_fallback)
    }

    override fun setDefaultSmsAppButtonOnClickListener() {
        defaultSmsAppButton.setOnClickListener({ presenter.onDefaultSmsAppButtonClicked() })
    }

    override fun setInfoText(numberOfSpamMessages: Int) {
        infoText.text = resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)
    }

    override fun initMessagesList(messages: List<Message>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessagesAdapter(messages)
    }

    override fun hasSmsPermission(): Boolean =
            ContextCompat.checkSelfPermission(this, READ_SMS_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun shouldShowRequestPermission(): Boolean =
            ActivityCompat.shouldShowRequestPermissionRationale(this, READ_SMS_PERMISSION)

    override fun showPermissionExplanation() {
        showSimpleError(R.string.please_allow_sms_permission, { requestPermission() })
    }

    override fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_SMS_PERMISSION), READ_SMS_PERMISSIONS_REQUEST)
    }

    private fun initViews() {
        defaultSmsAppButton = activity_messages_button
        infoText = activity_messages_number_of_filtered_messages
        recyclerView = activity_messages_recycleview
    }

}
