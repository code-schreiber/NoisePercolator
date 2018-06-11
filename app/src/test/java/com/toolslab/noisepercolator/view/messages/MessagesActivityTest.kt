package com.toolslab.noisepercolator.view.messages

import android.content.pm.PackageManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.toolslab.noisepercolator.R
import com.toolslab.noisepercolator.model.Message
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

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
        val captor = argumentCaptor<View.OnClickListener>()

        underTest.setDefaultSmsAppButtonOnClickListener()

        verify(underTest.defaultSmsAppButton).setOnClickListener(captor.capture())
        // Invoke the passed function as a parameter and then verify that the expected thing happens
        val passedFunction = captor.firstValue
        passedFunction.onClick(null)
        verify(mockPresenter).onDefaultSmsAppButtonClicked()
    }

    @Test
    fun initMessagesList() {
        underTest.recyclerView = mock()
        val captor = argumentCaptor<MessagesAdapter>()
        val expectedItemCount = 1234
        whenever(mockMessages.size).thenReturn(expectedItemCount)

        underTest.initMessagesList(mockMessages)

        verify(underTest.recyclerView).setHasFixedSize(true)
        verify(underTest.recyclerView).layoutManager = any<LinearLayoutManager>()
        verify(underTest.recyclerView).adapter = captor.capture()
        captor.firstValue.itemCount shouldEqual expectedItemCount
    }

    @Test
    fun showPermissionExplanation() {
        val expectedResId = R.string.please_allow_sms_permission
        val captor = argumentCaptor<() -> Unit>()
        val spyOfUnderTest = spy(underTest)
        doNothing().whenever(spyOfUnderTest).showSimpleError(anyInt(), any())
        doNothing().whenever(spyOfUnderTest).requestPermission()

        spyOfUnderTest.showPermissionExplanation()

        verify(spyOfUnderTest).showSimpleError(eq(expectedResId), captor.capture())
        // Invoke the passed function as a parameter and then verify that the expected thing happens
        val passedFunction = captor.firstValue
        passedFunction.invoke()
        verify(spyOfUnderTest).requestPermission()
    }

}
