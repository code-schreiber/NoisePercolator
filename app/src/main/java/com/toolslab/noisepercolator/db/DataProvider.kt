package com.toolslab.noisepercolator.db

import android.support.annotation.CheckResult
import com.toolslab.noisepercolator.model.Message
import io.reactivex.Observable
import timber.log.Timber

class DataProvider(private val realmWrapper: RealmWrapper = RealmWrapper()) {

    @CheckResult
    fun getMessages(): Observable<List<Message>> {
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

}
