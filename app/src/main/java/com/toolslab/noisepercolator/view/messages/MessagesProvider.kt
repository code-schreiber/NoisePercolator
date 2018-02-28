package com.toolslab.noisepercolator.view.messages

import android.annotation.TargetApi
import android.content.ContentResolver
import android.net.Uri
import android.provider.Telephony
import android.support.annotation.VisibleForTesting
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.device.SdkChecker
import timber.log.Timber


class MessagesProvider(private val sdkChecker: SdkChecker = SdkChecker(),
                       private val cursorToMessageConverter: CursorToMessageConverter = CursorToMessageConverter()) {

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
                messages.add(cursorToMessageConverter.convert(cursor))
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
                messages.add(cursorToMessageConverter.convert(cursor))
            } while (cursor.moveToNext())
        } else Timber.d("Empty inbox, no sms in $smsUri")
        cursor.close()
        return messages.toList()
    }

    private fun initSmsUri() {
        if (!this::smsUri.isInitialized) {
            smsUri = if (sdkChecker.deviceIsKitkatOrAbove()) Telephony.Sms.CONTENT_URI else Uri.parse("content://sms")
        }
    }

}
