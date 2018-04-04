package com.toolslab.noisepercolator.db

import io.realm.Realm

/**
 * Class that makes mocking easier when using [Realm.getDefaultInstance].
 */
class RealmWrapper {

    internal fun getDefaultInstance() = Realm.getDefaultInstance()
}
