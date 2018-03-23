package com.toolslab.noisepercolator.view.messages

import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.*
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.mockito.ArgumentCaptor

class MessagesActivityTest {

    private val mockPresenter: MessagesContract.Presenter = mock()
    private val mockMessages: List<Message> = mock()

    private val underTest = MessagesActivity(mockPresenter)

    @Test
    fun onRequestPermissionsResultGranted() {
        val requestCode = MessagesActivity.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)

        underTest.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verify(mockPresenter).smsPermissionsGranted()
        verify(mockPresenter, never()).onNoPermission()
    }

    @Test
    fun onRequestPermissionsResultDenied() {
        val requestCode = MessagesActivity.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)

        underTest.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verify(mockPresenter).onNoPermission()
        verify(mockPresenter, never()).smsPermissionsGranted()
    }

    @Test
    fun onRequestPermissionsResultWithEmptyGrantResults() {
        val requestCode = MessagesActivity.READ_SMS_PERMISSIONS_REQUEST
        val permissions = arrayOf<String>()
        val grantResults = intArrayOf()

        underTest.onRequestPermissionsResult(requestCode, permissions, grantResults)

        verify(mockPresenter).onNoPermission()
        verify(mockPresenter, never()).smsPermissionsGranted()
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

    @Test
    fun showPermissionExplanation() {
//        val resId = R.string.please_allow_sms_permission

//        underTest.showPermissionExplanation()

        // TODO implement test with spy
//        verify(mockBaseActivity).showSimpleError(eq(resId), any())
    }

}
