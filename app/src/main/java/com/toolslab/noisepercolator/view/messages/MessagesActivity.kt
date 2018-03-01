package com.toolslab.noisepercolator.view.messages

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.PermissionsUtil
import com.toolslab.noisepercolator.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_messages.*


class MessagesActivity(private val presenter: MessagesContract.Presenter = MessagesPresenter(),
                       private val permissionsUtil: PermissionsUtil = PermissionsUtil())
    : BaseActivity(), MessagesContract.View {

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
        if (permissionsUtil.isOnRequestPermissionsResultGranted(this, requestCode, permissions, grantResults)) {
            presenter.smsPermissionsGranted()
        }
    }

    override fun hasSmsPermission(): Boolean {
        return permissionsUtil.hasSmsPermission(this)
    }

    override fun maybeShowPermissionExplanation() {
        permissionsUtil.maybeShowPermissionExplanation(this)
    }

    override fun initDefaultSmsAppButton(text: String, onClickAction: () -> Unit) {
        defaultSmsAppButton.text = text
        defaultSmsAppButton.setOnClickListener({ onClickAction() })
    }

    override fun setInfoText(text: String) {
        infoText.text = text
    }

    override fun initMessagesList(messages: List<Message>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessagesAdapter(messages)
    }

    private fun initViews() {
        defaultSmsAppButton = activity_messages_button
        infoText = activity_messages_number_of_filtered_messages
        recyclerView = activity_messages_recycleview
    }

}
