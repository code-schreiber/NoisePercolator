package com.toolslab.noisepercolator.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.toolslab.noisepercolator.R


class MainActivity : AppCompatActivity() {

    companion object {
        private const val READ_SMS_PERMISSIONS_REQUEST = 112233
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (noPermission()) {
            getPermissionToReadSMS()
        } else {
            refreshSmsInbox();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            READ_SMS_PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show()
                    refreshSmsInbox();
                } else {
                    Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                Log.w("MainActivity", "Unknown RequestPermissionsResult: " + requestCode)
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun getPermissionToReadSMS() {
        if (noPermission()) {
            if (shouldShowRequestPermission()) {
                showPermissionExplanation()
            } else {
                // No explanation needed, we can request the permission.
                requestPermission()
            }
        }
    }

    private fun refreshSmsInbox() {
        val smsUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Telephony.Sms.CONTENT_URI
        } else {
            Uri.parse("content://sms/inbox") // undocumented content provider
        }
        val smsInboxCursor = contentResolver.query(smsUri, null, null, null, null)
        if (smsInboxCursor.moveToFirst()) {
            Toast.makeText(this, smsInboxCursor.getCount().toString() + " sms", Toast.LENGTH_SHORT).show()

            var smsAsString = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Toast.makeText(this, Telephony.Sms.getDefaultSmsPackage(this) + " is DefaultSmsPackage", Toast.LENGTH_SHORT).show()

                val date = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                val address = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val type = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
                val typeDescription =
                        when ((smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))).toInt()) {
                            Telephony.Sms.MESSAGE_TYPE_INBOX -> {
                                Telephony.Sms.MESSAGE_TYPE_INBOX.toString()
                            }
                            Telephony.Sms.MESSAGE_TYPE_SENT -> {
                                Telephony.Sms.MESSAGE_TYPE_SENT.toString()
                            }
                            else -> {
                                TODO("put type in string")
                                "unknown sms type"
                            }
                        }
                var debugInfo = ""
                for (i in 0 until smsInboxCursor.columnCount) {
                    debugInfo += smsInboxCursor.getColumnName(i) + ": " + smsInboxCursor.getString(i) + "; "
                }
                do {
                    smsAsString += address + "\n" +
                            date + "\n"
                    body + "\n"
                    debugInfo + "\n\n"
                } while (smsInboxCursor.moveToNext())
            } else {
                do {
                    for (i in 0 until smsInboxCursor.columnCount) {
                        smsAsString += smsInboxCursor.getColumnName(i) + ": " + smsInboxCursor.getString(i) + "; "
                    }
                    smsAsString += "\n\n"
                } while (smsInboxCursor.moveToNext())
            }

            this.findViewById<TextView>(R.id.activity_main_textdummy).setText(smsAsString)
        } else {
            Toast.makeText(this, "Empty inbox, no SMS", Toast.LENGTH_SHORT).show()
        }
        smsInboxCursor.close()
    }

    private fun showPermissionExplanation() {
        Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show()
    }

    private fun noPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermission() =
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)

    private fun requestPermission() =
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), READ_SMS_PERMISSIONS_REQUEST)


}
