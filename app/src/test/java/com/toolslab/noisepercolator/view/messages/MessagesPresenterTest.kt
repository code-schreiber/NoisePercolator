package com.toolslab.noisepercolator.view.messages

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.toolslab.noisepercolator.db.Persister
import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class MessagesPresenterTest {

    private val mockPackageManagerUtil: PackageManagerUtil = mock()
    private val mockMessagesProvider: MessagesProvider = mock()
    private val mockPersister: Persister = mock()
    private val mockView: MessagesContract.View = mock()

    private val underTest = MessagesPresenter(mockPackageManagerUtil, mockMessagesProvider, mockPersister)

    @Test
    fun onBound() {
        val defaultSmsAppName = "defaultSmsAppName"
        val expectedButtonText = "Open $defaultSmsAppName"
        val messages = listOf(dummyMessage(), dummyMessage())
        val numberOfFilteredMessages = 1
        val numberOfMessages = messages.size
        val expectedInfoText = "$numberOfFilteredMessages spam messages out of $numberOfMessages messages:"
        whenever(mockView.hasSmsPermission()).thenReturn(true)
        whenever(mockPackageManagerUtil.getDefaultSmsAppName()).thenReturn(defaultSmsAppName)
        whenever(mockMessagesProvider.getMessages()).thenReturn(messages)
        whenever(mockPersister.getNumberOfMessages()).thenReturn(numberOfFilteredMessages)

        underTest.onBound(mockView)

        verify(mockView).hasSmsPermission()
        verify(mockView).initDefaultSmsAppButton(expectedButtonText, any())//TODO how to test setOnClickListener?
        verify(mockView).setInfoText(expectedInfoText)
        verify(mockView).initMessagesList(messages)
        verifyNoMoreInteractions(mockView)
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
    fun unbind() {
        underTest.unbind(mockView)

        verifyZeroInteractions(mockView)
    }

    private fun dummyMessage() = Message("", "", "", "")

}
