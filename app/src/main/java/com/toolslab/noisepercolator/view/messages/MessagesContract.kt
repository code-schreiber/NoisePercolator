package com.toolslab.noisepercolator.view.messages

import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.mvp.BaseView
import com.toolslab.noisepercolator.mvp.MvpPresenter

interface MessagesContract {

    interface Presenter : MvpPresenter<View> {

        fun smsPermissionsGranted()

        fun onDefaultSmsAppButtonClicked()

        fun onNoPermission()
    }

    interface View : BaseView {

        fun hasSmsPermission(): Boolean

        fun setDefaultSmsAppButtonText(defaultSmsAppName: String)

        fun setDefaultSmsAppButtonTextFallback()

        fun setDefaultSmsAppButtonOnClickListener()

        fun setInfoText(numberOfSpamMessages: Int)

        fun initMessagesList(messages: List<Message>)

        fun shouldShowRequestPermission(): Boolean

        fun showPermissionExplanation()

        fun requestPermission()
    }
}
