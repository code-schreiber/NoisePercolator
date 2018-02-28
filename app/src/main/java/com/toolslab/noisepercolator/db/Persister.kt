package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import androidx.content.edit
import com.toolslab.noisepercolator.NoisePercolator

// TODO rename and test class

class Persister(private val context: Context = NoisePercolator.applicationContext()) {

    companion object {
        private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
        private const val NUMBER_OF_MESSAGES_KEY = "NUMBER_OF_MESSAGES_KEY"
    }

    fun getNumberOfMessages(): Int {
        return getInt(NUMBER_OF_MESSAGES_KEY, 0)
    }

    fun setNumberOfMessages(numberOfMessages: Int) {
        getPreferences().edit {
            putInt(NUMBER_OF_MESSAGES_KEY, numberOfMessages)
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
