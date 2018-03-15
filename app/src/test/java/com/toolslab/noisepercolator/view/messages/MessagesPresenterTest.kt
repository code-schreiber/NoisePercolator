package com.toolslab.noisepercolator.view.messages

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.db.DataProvider
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class MessagesPresenterTest {

    companion object {
        private const val DEFAULT_SMS_APP_NAME = "DEFAULT_SMS_APP_NAME"
        private val MESSAGES = listOf(dummyMessage(), dummyMessage())
        private val NUMBER_OF_MESSAGES = MESSAGES.size

        private fun dummyMessage() = Message("", 0L, "")
    }

    private val mockPackageManagerUtil: PackageManagerUtil = mock()
    private val mockDataProvider: DataProvider = mock()
    private val mockView: MessagesContract.View = mock()

    private val underTest = MessagesPresenter(mockPackageManagerUtil, mockDataProvider)

    @Before
    fun setUp() {
        whenever(mockPackageManagerUtil.getDefaultSmsAppName()).thenReturn(DEFAULT_SMS_APP_NAME)
        whenever(mockDataProvider.getMessages()).thenReturn(MESSAGES)
    }

    @Test
    fun onBound() {
        whenever(mockView.hasSmsPermission()).thenReturn(true)

        underTest.onBound(mockView)

        verifyInitView()
    }

    @Test
    fun onBoundWithEmptyDefaultSmsAppName() {
        whenever(mockView.hasSmsPermission()).thenReturn(true)
        whenever(mockPackageManagerUtil.getDefaultSmsAppName()).thenReturn("")

        underTest.onBound(mockView)

        verify(mockView).setDefaultSmsAppButtonTextFallback()
    }

    @Test
    fun onBoundWithoutSmsPermission() {
        whenever(mockView.hasSmsPermission()).thenReturn(false)

        underTest.onBound(mockView)

        verify(mockView).hasSmsPermission()
        verify(mockView).maybeShowPermissionExplanation()
        verifyNoMoreInteractions(mockView)
    }

    @Test
    fun smsPermissionsGranted() {
        underTest.bind(mockView)
        reset(mockView)

        underTest.smsPermissionsGranted()

        verifyInitView()
    }

    @Test
    fun onDefaultSmsAppButtonClicked() {
        underTest.onDefaultSmsAppButtonClicked()

        verify(mockPackageManagerUtil).launchDefaultSmsApp()
    }

    @Test
    fun unbind() {
        underTest.unbind(mockView)

        verifyZeroInteractions(mockView)
    }

    private fun verifyInitView() {
        verify(mockView).setDefaultSmsAppButtonText(DEFAULT_SMS_APP_NAME)
        verify(mockView).setDefaultSmsAppButtonOnClickListener()
        verify(mockView).setInfoText(NUMBER_OF_MESSAGES)
        verify(mockView).initMessagesList(MESSAGES)
    }

}
