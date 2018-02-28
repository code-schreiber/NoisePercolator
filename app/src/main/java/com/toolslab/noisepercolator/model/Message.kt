package com.toolslab.noisepercolator.model

class Message(val address: String, val date: String, val body: String, val debugInfo: String) {

    var spam: Boolean = false

    fun markAsSpam() {
        spam = true
    }

}
