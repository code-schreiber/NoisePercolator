package com.toolslab.noisepercolator.view.messages

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.db.Persister
import com.toolslab.noisepercolator.util.PermissionsUtil
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
import com.toolslab.noisepercolator.view.base.BaseActivity


class MessagesActivity : BaseActivity() {

    private val persister = Persister()
    private val messageProvider = MessagesProvider()
    private val permissionsUtil = PermissionsUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (permissionsUtil.hasSmsPermission(this)) {
            refreshSmsInbox()
        } else {
            permissionsUtil.maybeShowPermissionExplanation(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (permissionsUtil.isOnRequestPermissionsResultGranted(this, requestCode, permissions, grantResults)) {
            refreshSmsInbox()
        }
    }

    private fun refreshSmsInbox() {
        val otherAppHandler = PackageManagerUtil()
        this.findViewById<TextView>(R.id.activity_messages_button).text = "Open " + otherAppHandler.getDefaultSmsAppName()
        this.findViewById<TextView>(R.id.activity_messages_button).setOnClickListener({
            otherAppHandler.launchDefaultSmsApp()
        })

        val messages = messageProvider.getMessages(contentResolver)
        val numberOfFilteredMessages = persister.getNumberOfMessages()
        val numberOfMessages = messages.size
        this.findViewById<TextView>(R.id.activity_messages_number_of_filtered_messages).text = "$numberOfFilteredMessages spam messages out of $numberOfMessages messages:"
        this.findViewById<RecyclerView>(R.id.activity_messages_recycleview).setHasFixedSize(true)
        this.findViewById<RecyclerView>(R.id.activity_messages_recycleview).layoutManager = LinearLayoutManager(this)
        this.findViewById<RecyclerView>(R.id.activity_messages_recycleview).adapter = MessagesAdapter(messages)
    }

}
