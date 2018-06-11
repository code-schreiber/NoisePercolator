package com.toolslab.noisepercolator

import android.app.Application
import android.content.Context
import io.realm.Realm
import timber.log.Timber
import timber.log.Timber.DebugTree


class NoisePercolator : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: NoisePercolator? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}
