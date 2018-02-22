package com.toolslab.noisepercolator.db

import android.content.Context
import android.content.SharedPreferences
import com.toolslab.noisepercolator.NoisePercolator

class Persister(private val context: Context = NoisePercolator.applicationContext()) {

    companion object {
        private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
        private const val NUMBER_OF_MESSAGES_KEY = "NUMBER_OF_MESSAGES_KEY"
    }

    fun setNumberOfMessages(numberOfMessages: Int) {
        getEditor().putInt(NUMBER_OF_MESSAGES_KEY, numberOfMessages).commit()
    }

    fun getNumberOfMessages(): Int {
        return getPreferences().getInt(NUMBER_OF_MESSAGES_KEY, 0)
    }

    fun clearPreferences() {
        getEditor().clear().commit()
    }

    private fun getEditor(): SharedPreferences.Editor {
        return getPreferences().edit()
    }

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

}
