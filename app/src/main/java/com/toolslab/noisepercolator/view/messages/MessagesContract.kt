package com.toolslab.noisepercolator.view.messages

import com.toolslab.noisepercolator.model.Message
import com.toolslab.noisepercolator.mvp.BaseView
import com.toolslab.noisepercolator.mvp.MvpPresenter

interface MessagesContract {

    interface Presenter : MvpPresenter<View> {

        fun smsPermissionsGranted()

        fun onDefaultSmsAppButtonClicked()
    }

    interface View : BaseView {

        fun hasSmsPermission(): Boolean

        fun maybeShowPermissionExplanation()

        fun setDefaultSmsAppButtonText(text: String)

        fun setDefaultSmsAppButtonOnClickListener()

        fun setInfoText(text: String)

        fun initMessagesList(messages: List<Message>)
    }
}
