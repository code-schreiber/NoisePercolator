package com.toolslab.noisepercolator.view.messages

import android.annotation.SuppressLint
import android.database.Cursor
import android.provider.Telephony
import android.support.annotation.RequiresApi
import android.support.annotation.VisibleForTesting
import androidx.database.getString
import com.toolslab.noisepercolator.filter.SmsFilter
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.device.SdkChecker
import java.text.DateFormat
import java.util.*

@SuppressLint("NewApi")
class CursorToMessageConverter(private val smsFilter: SmsFilter = SmsFilter(),
                               private val sdkChecker: SdkChecker = SdkChecker()) {

    companion object {

        @VisibleForTesting
        @RequiresApi(SdkChecker.KITKAT)
        const val COLUMN_ADDRESS = Telephony.Sms.ADDRESS

        @VisibleForTesting
        @RequiresApi(SdkChecker.KITKAT)
        const val COLUMN_DATE = Telephony.Sms.DATE

        @VisibleForTesting
        @RequiresApi(SdkChecker.KITKAT)
        const val COLUMN_BODY = Telephony.Sms.BODY

        private const val LEGACY_COLUMN_ADDRESS = "address"
        private const val LEGACY_COLUMN_DATE = "date"
        private const val LEGACY_COLUMN_BODY = "body"
    }

    internal fun convert(cursor: Cursor): Message {
        val address = cursor.getString(getColumnAddress())
        val date = extractDate(cursor)
        val body = cursor.getString(getColumnBody())
        val debugInfo = extractDebugInfo(cursor)
        val message = Message(address, date, body, debugInfo)
        if (smsFilter.isSpam(message)) {
            message.markAsSpam()
        }
        return message
    }

    private fun extractDate(cursor: Cursor): String {
        val date = cursor.getString(getColumnDate())
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(date.toLong()))
    }

    private fun extractDebugInfo(cursor: Cursor): String {
        var debugInfo = ""
        for (i in 0 until cursor.columnCount) {
            debugInfo += cursor.getColumnName(i) + ": " + cursor.getString(i) + "; "
        }
        return debugInfo
    }

    private fun getColumnAddress(): String {
        return if (sdkChecker.deviceIsKitkatOrAbove()) COLUMN_ADDRESS else LEGACY_COLUMN_ADDRESS
    }

    private fun getColumnDate(): String {
        return if (sdkChecker.deviceIsKitkatOrAbove()) COLUMN_DATE else LEGACY_COLUMN_DATE
    }

    private fun getColumnBody(): String {
        return if (sdkChecker.deviceIsKitkatOrAbove()) COLUMN_BODY else LEGACY_COLUMN_BODY
    }

}
