package com.toolslab.noisepercolator.view.messages

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.PermissionsUtil
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify

class MessagesActivityTest {

    private val mockPresenter: MessagesContract.Presenter = mock()
    private val mockPermissionsUtil: PermissionsUtil = mock()
    private val mockMessages: List<Message> = mock()

    private val underTest = MessagesActivity(mockPresenter, mockPermissionsUtil)

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

        verify(mockPermissionsUtil).maybeShowPermissionExplanation(underTest)
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
    fun setDefaultSmsAppButtonTextFallback() {
        underTest.defaultSmsAppButton = mock()

        underTest.setDefaultSmsAppButtonTextFallback()

        verify(underTest.defaultSmsAppButton).setText(R.string.open_sms_app_fallback)
    }

    @Test
    fun setDefaultSmsAppButtonOnClickListener() {
        underTest.defaultSmsAppButton = mock()

        underTest.setDefaultSmsAppButtonOnClickListener()

        verify(underTest.defaultSmsAppButton).setOnClickListener(any())
    }

    @Test
    fun initMessagesList() {
        val expectedItemCount = 1234
        whenever(mockMessages.size).thenReturn(expectedItemCount)
        val captor = ArgumentCaptor.forClass<MessagesAdapter, MessagesAdapter>(MessagesAdapter::class.java)
        underTest.recyclerView = mock()

        underTest.initMessagesList(mockMessages)

        verify(underTest.recyclerView).setHasFixedSize(true)
        verify(underTest.recyclerView).layoutManager = any()
        verify(underTest.recyclerView).adapter = captor.capture()
        captor.value.itemCount shouldEqual expectedItemCount
    }

}
