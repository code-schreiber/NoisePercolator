package com.toolslab.noisepercolator.view

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
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
                Log.e("MainActivity", "Unknown RequestPermissionsResult: " + requestCode)
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
        val smsUri = getSmsUri()
        val cursor = contentResolver.query(smsUri, null, null, null, null)
        if (cursor.moveToFirst()) {
            var smsAsString = cursor.count.toString() + " sms in " + smsUri + "\n"


            val messageId = cursor.getString(cursor.getColumnIndex("_id"))
            val values = ContentValues()
            values.put("read", true)
            val rowsUpdated = contentResolver.update(smsUri, values, "_id" + "=" + messageId, null)
            smsAsString += rowsUpdated.toString() + " rows updated" + "\n"

            smsAsString += packageName + " is this package" + "\n"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                smsAsString += extractSmsAsString(cursor)
            } else {
                smsAsString += extractSmsAsStringForDevicesOlderThanKitkat(cursor)
            }

            this.findViewById<TextView>(R.id.activity_main_textdummy).text = smsAsString
        } else {
            this.findViewById<TextView>(R.id.activity_main_textdummy).text = "Empty inbox, no SMS"
        }
        cursor.close()
    }

    private fun getSmsUri(): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Telephony.Sms.CONTENT_URI
        } else {
            Uri.parse("content://sms/inbox") // undocumented content provider (maybe use Telephony.Sms.CONTENT_URI also?)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun extractSmsAsString(cursor: Cursor): String {
        var smsAsString = ""
        smsAsString += Telephony.Sms.getDefaultSmsPackage(this) + " is DefaultSmsPackage" + "\n\n"

        do {
            val date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
            val typeDescription =
                    when (type.toInt()) {
                        Telephony.Sms.MESSAGE_TYPE_INBOX -> {
                            Telephony.Sms.MESSAGE_TYPE_INBOX.toString()
                        }
                        Telephony.Sms.MESSAGE_TYPE_SENT -> {
                            Telephony.Sms.MESSAGE_TYPE_SENT.toString()
                        }
                        else -> {
//                            TODO("put type in string")
                            "unknown sms type"
                        }
                    }

            var debugInfo = ""
            for (i in 0 until cursor.columnCount) {
                debugInfo += cursor.getColumnName(i) + ": " + cursor.getString(i) + "; "
            }

            smsAsString += address + "\n" +
                    date + "\n" +
                    body + "\n" +
                    typeDescription + "\n" +
                    debugInfo + "\n\n"
        } while (cursor.moveToNext())
        return smsAsString
    }

    private fun extractSmsAsStringForDevicesOlderThanKitkat(cursor: Cursor): String {
        var smsAsString = ""
        do {
            for (i in 0 until cursor.columnCount) {
                smsAsString += cursor.getColumnName(i) + ": " + cursor.getString(i) + "; "
            }
            smsAsString += "\n\n"
        } while (cursor.moveToNext())
        return smsAsString
    }

    private fun showPermissionExplanation() =
            Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show()

    private fun noPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermission() =
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)

    private fun requestPermission() =
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), READ_SMS_PERMISSIONS_REQUEST)


}
