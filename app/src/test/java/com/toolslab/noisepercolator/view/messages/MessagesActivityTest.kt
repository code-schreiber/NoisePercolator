package com.toolslab.noisepercolator.view.messages

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.PermissionsUtil
import org.amshove.kluent.shouldEqual
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.verify

class MessagesActivityTest {

    private val mockPresenter: MessagesContract.Presenter = mock()
    private val mockPermissionsUtil: PermissionsUtil = mock()
    private val mockMessages: List<Message> = mock()

    private val underTest = MessagesActivity(mockPresenter, mockPermissionsUtil)

    @Ignore("How to verify bind and unbind?")
    @Test
    fun viewBindsItself() {

    }

    @Test
    fun onRequestPermissionsResultGranted() {
        val requestCode = 0
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()
        whenever(mockPermissionsUtil.isOnRequestPermissionsResultGranted(underTest, requestCode, permissions, grantResults))
                .thenReturn(true)

        underTest.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verify(mockPresenter).smsPermissionsGranted()
    }

    @Test
    fun onRequestPermissionsResultDenied() {
        val requestCode = 0
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()
        whenever(mockPermissionsUtil.isOnRequestPermissionsResultGranted(underTest, requestCode, permissions, grantResults))
                .thenReturn(false)

        underTest.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verifyZeroInteractions(mockPresenter)
    }

    @Test
    fun hasSmsPermission() {
        val expected = true
        whenever(mockPermissionsUtil.hasSmsPermission(underTest)).thenReturn(expected)

        val result = underTest.hasSmsPermission()

        result shouldEqual expected
    }

    @Test
    fun maybeShowPermissionExplanation() {
        underTest.maybeShowPermissionExplanation()

        verify(mockPermissionsUtil).maybeShowPermissionExplanation(underTest)
    }

    @Test
    fun initDefaultSmsAppButton() {
        val mockAction: () -> Unit = mock()
        underTest.defaultSmsAppButton = mock()
        val text = "a text"

        underTest.initDefaultSmsAppButton(text, mockAction)

        verify(underTest.defaultSmsAppButton).text = text
//        verify(underTest.defaultSmsAppButton).setOnClickListener { mockAction() } TODO how to test setOnClickListener?
    }

    @Test
    fun setInfoText() {
        underTest.infoText = mock()
        val text = "a text"

        underTest.setInfoText(text)

        verify(underTest.infoText).text = text
    }

    @Test
    fun initMessagesList() {
        underTest.recyclerView = mock()

        underTest.initMessagesList(mockMessages)

        verify(underTest.recyclerView).setHasFixedSize(true)
        verify(underTest.recyclerView).layoutManager = any()
        verify(underTest.recyclerView).adapter = any() // TODO test that that any was passed messages to constructor MessagesAdapter(messages)
    }

}
