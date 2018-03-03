package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import androidx.content.edit
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.messages.MessageConverter

class DataProvider(private val context: Context = NoisePercolator.applicationContext(),
                   private val messageConverter: MessageConverter = MessageConverter()) {

    companion object {

        @VisibleForTesting
        const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"

        @VisibleForTesting
        const val NUMBER_OF_MESSAGES_KEY = "NUMBER_OF_MESSAGES_KEY"

        @VisibleForTesting
        const val MESSAGES_KEY = "MESSAGES_KEY"
    }

    @CheckResult
    fun getNumberOfMessages(): Int {
        return getInt(NUMBER_OF_MESSAGES_KEY, 0)
    }

    fun setNumberOfMessages(numberOfMessages: Int) {
        getPreferences().edit {
            putInt(NUMBER_OF_MESSAGES_KEY, numberOfMessages)
        }
    }

    @CheckResult
    fun getMessages(): List<Message> {
        val messagesAsStringSet = getMessagesStringSet()
        return messagesAsStringSet.map { messageConverter.convert(it) }
    }

    @CheckResult
    fun getMessagesStringSet(): MutableSet<String> {
        return getPreferences().getStringSet(MESSAGES_KEY, emptySet())
    }

    fun saveMessage(message: Message) {
        val messages = getMessagesStringSet()
        messages.add(messageConverter.convert(message))
        getPreferences().edit {
            putStringSet(MESSAGES_KEY, messages)
        }
    }

    fun clearPreferences() {
        getPreferences().edit {
            clear()
        }
    }

    private fun getInt(key: String, defValue: Int) = getPreferences().getInt(key, defValue)

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

}
