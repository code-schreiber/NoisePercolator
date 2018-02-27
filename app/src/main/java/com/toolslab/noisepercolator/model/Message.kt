package com.toolslab.noisepercolator.model

class Message(val address: String, val body: String, val date: String, val debugInfo: String) {

    var spam: Boolean = false

    fun markAsSpam() {
        spam = true
    }

}
