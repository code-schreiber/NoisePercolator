package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.CheckResult
import android.support.annotation.VisibleForTesting
import androidx.content.edit
import com.toolslab.noisepercolator.NoisePercolator
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.view.messages.MessageConverter
import io.reactivex.Observable
import timber.log.Timber

class DataProvider(private val context: Context = NoisePercolator.applicationContext(),
                   private val messageConverter: MessageConverter = MessageConverter(),
                   private val realmWrapper: RealmWrapper = RealmWrapper()) {

    companion object {

        @VisibleForTesting
        const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"

        @VisibleForTesting
        const val MESSAGES_KEY = "MESSAGES_KEY"

        const val SHARED_PREFERENCES_DEPRECATED_MESSAGE = "Now using Realm instead of SharedPreferences."
    }

    @CheckResult
    fun getMessages(): Observable<List<Message>> {
        migrateOldDatabaseIfNeeded()
        return realmWrapper.getDefaultInstance().run {
            this.where(Message::class.java).findAllAsync()
                    .asFlowable()
                    .toObservable()
                    .map {
                        this.copyFromRealm(it)
                    }
                    .doOnDispose {
                        // Close database when observable is not needed anymore
                        this.close()
                    }
                    .doOnError {
                        // TODO ASK Close database?
                        Timber.e(it, "Error in DataProvider.getMessages()")
                    }
        }
    }

    fun saveMessages(messages: List<Message>) {
        messages.forEach { saveMessage(it) }
    }

    fun saveMessage(message: Message) {
        realmWrapper.getDefaultInstance().run {
            // Persist your data in a transaction
            this.executeTransaction {
                // Using executeTransaction with a lambda reduces code size
                // and makes it impossible to forget to commit the transaction.
                it.copyToRealm(message)
            }
            // Close database after saving
            this.close()
        }
    }

    @Deprecated("$SHARED_PREFERENCES_DEPRECATED_MESSAGE Use DataProvider.getMessages().",
            ReplaceWith("getMessages()", "com.toolslab.noisepercolator.db.DataProvider.getMessages"))
    fun getMessagesFromSharedPreferences(): List<Message> {
        val messagesAsStringSet = getMessagesStringSet()
        return messagesAsStringSet.map { messageConverter.convert(it) }
    }

    @Deprecated(SHARED_PREFERENCES_DEPRECATED_MESSAGE)
    @VisibleForTesting
    fun getMessagesStringSet(): MutableSet<String> {
        return getPreferences().getStringSet(MESSAGES_KEY, mutableSetOf())
    }

    @Deprecated("$SHARED_PREFERENCES_DEPRECATED_MESSAGE Use DataProvider.saveMessage(message).",
            ReplaceWith("saveMessage(message)", "com.toolslab.noisepercolator.db.DataProvider.saveMessage"))
    fun saveMessageToSharedPreferences(message: Message) {
        val messages = getMessagesStringSet().toMutableSet()
        messages.add(messageConverter.convert(message))
        getPreferences().edit {
            putStringSet(MESSAGES_KEY, messages)
        }
    }

    private fun migrateOldDatabaseIfNeeded() {
        if (getPreferences().contains(MESSAGES_KEY)) {
            val messages = getMessagesFromSharedPreferences()
            Timber.d("Database with %d items will be migrated", messages.size)
            saveMessages(messages)
            clearSharedPreferences()
            Timber.d("Database was migrated")
        }
    }

    @Deprecated(SHARED_PREFERENCES_DEPRECATED_MESSAGE)
    fun clearSharedPreferences() {
        getPreferences().edit {
            clear()
        }
    }

    @Deprecated(SHARED_PREFERENCES_DEPRECATED_MESSAGE)
    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

}
