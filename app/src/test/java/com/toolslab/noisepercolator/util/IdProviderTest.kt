package com.toolslab.noisepercolator.util

import org.amshove.kluent.shouldEqual
import org.junit.Test

class IdProviderTest {

    private val underTest = IdProvider()

    @Test
    fun createIdFromNormalNumber() {
        val number = "12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromMaxInt() {
        val number = Int.MAX_VALUE.toString()
        val expected = 147483647 // the last digits of Int max: 2 147483647

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromBiggerThanMaxInt() {
        val number = Long.MAX_VALUE.toString()
        val expected = 854775807 // the last digits of Long max: 9223372036 854775807

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromNumberContainingMinus() {
        val number = "-12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromNumberContainingPlus() {
        val number = "+12"
        val expected = 12

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromOnlyText() {
        val number = "only text, no numbers"
        val expected = 305005312 // is number.hasCode()

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun createIdFromNumbersInSymbols() {
        val number = "+„¡“¶¢[]|{}≠¿'«∑€®†Ω¨øπ•±å‚∂ƒ©ªº∆@œ123æ‘≤¥≈ç√∫~∞…–°!§$%&/(\n)=?"
        val expected = 123

        val result = underTest.createIdFrom(number)

        result shouldEqual expected
    }

    @Test
    fun idShouldBeTheSameForSameString() {
        val number = "the same string"

        val id1 = underTest.createIdFrom(number)
        val id2 = underTest.createIdFrom(number)
        val id3 = underTest.createIdFrom(number)

        id1 shouldEqual id2
        id2 shouldEqual id3
        id1 shouldEqual id3
    }

}
