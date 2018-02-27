package com.toolslab.noisepercolator.view.messages

import android.annotation.TargetApi
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import android.support.annotation.VisibleForTesting
import androidx.database.getString
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber


class MessagesProvider(private val sdkChecker: SdkChecker = SdkChecker(),
                       private val smsFilter: SmsFilter = SmsFilter()) {

    @VisibleForTesting
    lateinit var smsUri: Uri // TODO there is a better way of testing without lateinit

    fun getMessages(contentResolver: ContentResolver): List<Message> {
        return if (sdkChecker.deviceIsKitkatOrAbove()) {
            extractSmsAsString(contentResolver)
        } else {
            extractSmsAsStringLegacy(contentResolver)
        }
    }

    @TargetApi(SdkChecker.KITKAT)
    private fun extractSmsAsString(contentResolver: ContentResolver): List<Message> {
        initSmsUri()
        val messages = mutableListOf<Message>()
        val cursor = contentResolver.query(smsUri, null, null, null, null)
        if (cursor.moveToFirst()) {
            Timber.d("$cursor.count.toString() sms in $smsUri")
            do {
                messages.add(convertCursorToMessage(cursor))
            } while (cursor.moveToNext())
        } else Timber.d("Empty inbox, no sms in $smsUri")
        cursor.close()
        return messages.toList()
    }

    private fun extractSmsAsStringLegacy(contentResolver: ContentResolver): List<Message> {
        initSmsUri()
        val messages = mutableListOf<Message>()
        val cursor = contentResolver.query(smsUri, null, null, null, null)
        if (cursor.moveToFirst()) {
            val smsCount = cursor.count
            Timber.d("$smsCount sms in $smsUri")
            do {
                messages.add(convertCursorToMessage(cursor))
            } while (cursor.moveToNext())
        } else Timber.d("Empty inbox, no sms in $smsUri")
        cursor.close()
        return messages.toList()
    }

    private fun convertCursorToMessage(cursor: Cursor): Message {
        val date = cursor.getString(Telephony.Sms.DATE)
        val address = cursor.getString(Telephony.Sms.ADDRESS)
        val body = cursor.getString(Telephony.Sms.BODY)
        var debugInfo = ""
        for (i in 0 until cursor.columnCount) {
            debugInfo += cursor.getColumnName(i) + ": " + cursor.getString(i) + "; "
        }
        val message = Message(address, body, date, debugInfo)
        if (smsFilter.isSpam(message)) {
            message.markAsSpam()
        }
        return message
    }

    private fun initSmsUri() {
        if (!this::smsUri.isInitialized) {
            smsUri = if (sdkChecker.deviceIsKitkatOrAbove()) Telephony.Sms.CONTENT_URI else Uri.parse("content://sms")
        }
    }

}
