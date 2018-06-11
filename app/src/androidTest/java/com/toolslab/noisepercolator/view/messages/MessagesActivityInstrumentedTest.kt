package com.toolslab.noisepercolator.view.messages

import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.toolslab.noisepercolator.R
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessagesActivityInstrumentedTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MessagesActivity::class.java)

    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(MessagesActivity.READ_SMS_PERMISSION)

    private lateinit var underTest: MessagesActivity

    @Before
    fun setUp() {
        underTest = activityRule.activity
    }

    @UiThreadTest
    @Test
    fun setDefaultSmsAppButtonText() {
        val defaultSmsAppName = "defaultSmsAppName"
        val expected = underTest.getString(R.string.open_default_sms_app, defaultSmsAppName)

        underTest.setDefaultSmsAppButtonText(defaultSmsAppName)

        underTest.defaultSmsAppButton.text.toString() shouldEqual expected
    }

    @UiThreadTest
    @Test
    fun setInfoText() {
        val numberOfSpamMessages = 12
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        underTest.infoText.text.toString() shouldEqual expected
    }

    @UiThreadTest
    @Test
    fun setInfoTextToOne() {
        val numberOfSpamMessages = 1
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        underTest.infoText.text.toString() shouldEqual expected
    }

    @UiThreadTest
    @Test
    fun setInfoTextToZero() {
        val numberOfSpamMessages = 0
        val expected = underTest.resources.getQuantityString(R.plurals.number_of_spam_messages, numberOfSpamMessages, numberOfSpamMessages)

        underTest.setInfoText(numberOfSpamMessages)

        underTest.infoText.text.toString() shouldEqual expected
    }

    @Test
    fun hasSmsPermission() {
        val expected = true

        val result = underTest.hasSmsPermission()

        result shouldEqual expected
    }

    @Test
    fun shouldShowRequestPermission() {
        val expected = false

        val result = underTest.shouldShowRequestPermission()

        result shouldEqual expected
    }

}
