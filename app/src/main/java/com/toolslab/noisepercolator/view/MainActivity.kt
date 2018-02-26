package com.toolslab.noisepercolator.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.db.Persister
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    companion object {
        private const val READ_SMS_PERMISSIONS_REQUEST = 100
    }

    private val persister = Persister()
    private val messageProvider = MessagesProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (noPermission()) {
            maybeShowPermissionExplanation()
        } else {
            refreshSmsInbox()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            READ_SMS_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show()
                    refreshSmsInbox()
                } else {
                    Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                Timber.e("Unknown RequestPermissionsResult: $requestCode")
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun maybeShowPermissionExplanation() {
        if (noPermission()) {
            if (shouldShowRequestPermission()) {
                showPermissionExplanation()
            } else {
                // No explanation needed, we can request the permission.
                requestPermission()
            }
        }
    }

    private fun showPermissionExplanation() =
            Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show()

    private fun noPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermission() =
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)

    private fun requestPermission() =
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), READ_SMS_PERMISSIONS_REQUEST)

    private fun refreshSmsInbox() {
        val otherAppHandler = PackageManagerUtil()
        this.findViewById<TextView>(R.id.activity_main_button).text = "Open sms app: " + otherAppHandler.getDefaultSmsAppName()
        this.findViewById<TextView>(R.id.activity_main_button).setOnClickListener({
            otherAppHandler.launchDefaultSmsApp()
        })

        val numberOfFilteredMessages = persister.getNumberOfMessages()
        this.findViewById<TextView>(R.id.activity_main_number_of_filtered_messages).text = "$numberOfFilteredMessages filtered messages"

        val messages = messageProvider.getMessages(contentResolver)
        this.findViewById<RecyclerView>(R.id.activity_main_recycleview).setHasFixedSize(true)
        this.findViewById<RecyclerView>(R.id.activity_main_recycleview).setLayoutManager(LinearLayoutManager(this));
        this.findViewById<RecyclerView>(R.id.activity_main_recycleview).adapter = SmsListAdapter(messages)
    }

}
