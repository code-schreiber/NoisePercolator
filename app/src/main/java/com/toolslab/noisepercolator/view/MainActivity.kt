package com.toolslab.noisepercolator.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.toolslab.noisepercolator.R


class MainActivity : AppCompatActivity() {

    private val readSmsPermissionsRequest = 112233

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (noPermission()) {
            getPermissionToReadSMS()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == readSmsPermissionsRequest) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getPermissionToReadSMS() {
        if (noPermission()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(Manifest.permission.READ_SMS), readSmsPermissionsRequest)
        }
    }

    private fun noPermission() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED

}
