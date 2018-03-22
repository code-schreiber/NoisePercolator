package com.toolslab.noisepercolator.receiver

import android.content.Intent
import android.support.test.runner.AndroidJUnit4
import android.telephony.SmsMessage
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmsMessagesConverterInstrumentedTest {

    private companion object {
        private const val EXPECTED_MESSAGE = "Hello world! I'm a test sms"
        private const val EXPECTED_ADDRESS = "+1234567"
        private const val EXPECTED_PDUS_LIST_SIZE = 1
        private val PDU = byteArrayOf(0, 32, 7, -111, 33, 67, 101, -9, 0, 0, -127, 32, 98, 65, 100, 80, 0, 27, -56, 50, -101, -3, 6, -35, -33, 114, 54, 57, 4, 74, -98, -38, -96, 48, -120, 94, -98, -45, 65, -13, -10, 28)
    }

    private val intent = Intent()

    private val underTest: SmsMessagesConverter = SmsMessagesConverter()

    @Before
    fun setUp() {
        intent.putExtra(SmsMessagesConverter.PDUS_KEY, arrayOf(PDU))
    }

    @Ignore("Telephony.Sms.Intents.getMessagesFromIntent() returns a list with a null object on tests but it works on production")
    @Test
    fun convert() {
        val result = underTest.convert(intent)

        assertMessageIsCorrect(result)
    }

    @Test
    fun convertLegacy() {
        val result = underTest.convertLegacy(intent.extras)

        assertMessageIsCorrect(result)
    }

    private fun assertMessageIsCorrect(result: List<SmsMessage>) {
        assertThat(result.size, `is`(EXPECTED_PDUS_LIST_SIZE))
        assertThat(result[0], `is`(not(nullValue())))
        result[0].apply {
            assertThat(messageBody, `is`(EXPECTED_MESSAGE))
            assertThat(displayMessageBody, `is`(EXPECTED_MESSAGE))
            assertThat(originatingAddress, `is`(EXPECTED_ADDRESS))
            assertThat(displayOriginatingAddress, `is`(EXPECTED_ADDRESS))
        }
    }

}
