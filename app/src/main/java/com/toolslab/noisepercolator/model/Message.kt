package com.toolslab.noisepercolator.model

import io.realm.RealmObject
import java.text.DateFormat
import java.util.*

open class Message(internal var address: String,
                   internal var date: Long,
                   internal var body: String)
    : RealmObject(), Comparable<Message> {

    // Constructor needed for Realm
    constructor() : this("", 0, "")

    fun getFormattedDate(): String = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(date))

    override fun compareTo(other: Message) = compareValuesBy(other, this, { it.date })

}
