package com.toolslab.noisepercolator.view.messages

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.toolslab.noisepercolator.R
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MessagesActivityInstrumentedTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MessagesActivity::class.java)

    private lateinit var underTest: MessagesActivity

    @Before
    fun setUp() {
        underTest = activityRule.activity
    }

    @Test
    fun setDefaultSmsAppButtonText() {
        val defaultSmsAppName = "defaultSmsAppName"
        val expected = underTest.getString(R.string.open_default_sms_app, defaultSmsAppName)

        underTest.setDefaultSmsAppButtonText(defaultSmsAppName)

        assertThat(underTest.defaultSmsAppButton.text.toString(), `is`(expected))
    }

    @Test
    fun setInfoText() {
        val numberOfSpamMessages = 12
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        assertThat(underTest.infoText.text.toString(), `is`(expected))
    }

    @Test
    fun setInfoTextToOne() {
        val numberOfSpamMessages = 1
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        assertThat(underTest.infoText.text.toString(), `is`(expected))
    }

    @Test
    fun setInfoTextToZero() {
        val numberOfSpamMessages = 0
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        assertThat(underTest.infoText.text.toString(), `is`(expected))
    }

}
