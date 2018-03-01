package com.toolslab.noisepercolator.util

class IdProvider {

    fun createIdFrom(displayOriginatingAddress: String): Int {
        val leaveOnlyNumbersRegex = "\\D+".toRegex()
        val onlyNumbers = displayOriginatingAddress.replace(leaveOnlyNumbersRegex, "")
        if (onlyNumbers.isNotEmpty()) {
            val maxLength = Int.MAX_VALUE.toString().length - 1
            val startIndex = Math.max(onlyNumbers.length, maxLength) - maxLength
            return onlyNumbers.substring(startIndex).toInt()
        }
        // For addresses that don't have numbers
        // hasCode is a reasonable way of getting the same int for the same string,
        // but it doesn't assure uniqueness
        return displayOriginatingAddress.hashCode()
    }

}
