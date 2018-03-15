package com.toolslab.noisepercolator.view.base

import com.nhaarman.mockito_kotlin.mock
import com.toolslab.noisepercolator.view.common.DialogFactory
import org.junit.Test
import org.mockito.Mockito.verify

class BaseActivityTest {

    private val mockDialogFactory: DialogFactory = mock()

    private val underTest = BaseActivity(mockDialogFactory)

    @Test
    fun showSimpleDialog() {
        val input = "some text"

        underTest.showSimpleDialog(input)

        verify(mockDialogFactory).withMessage(underTest, input)
    }

    @Test
    fun showSimpleError() {
        val input = "some error"
        val onOkAction = {}

        underTest.showSimpleError(input, onOkAction)

        verify(mockDialogFactory).withMessage(underTest, input, onOkAction)
    }

}
