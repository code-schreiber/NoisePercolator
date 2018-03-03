package com.toolslab.noisepercolator.view.messages

import com.toolslab.noisepercolator.db.DataProvider
import com.toolslab.noisepercolator.mvp.BasePresenter
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil

class MessagesPresenter(private val packageManagerUtil: PackageManagerUtil = PackageManagerUtil(),
                        private val dataProvider: DataProvider = DataProvider())
    : BasePresenter<MessagesContract.View>(), MessagesContract.Presenter {

    override fun onBound(view: MessagesContract.View) {
        if (view.hasSmsPermission()) {
            initView(view)
        } else {
            view.maybeShowPermissionExplanation()
        }
    }

    override fun smsPermissionsGranted() {
        initView(getView())
    }

    private fun initView(view: MessagesContract.View) {
        val defaultSmsAppName = packageManagerUtil.getDefaultSmsAppName()
        val messages = dataProvider.getMessages()
        val numberOfMessages = messages.size
        val numberOfFilteredMessages = dataProvider.getNumberOfMessages()
        val defaultSmsAppButtonText = "Open $defaultSmsAppName"
        val infoText = "$numberOfFilteredMessages spam messages out of $numberOfMessages messages:"

        view.initDefaultSmsAppButton(defaultSmsAppButtonText, { packageManagerUtil.launchDefaultSmsApp() })
        view.setInfoText(infoText)
        view.initMessagesList(messages)
    }

}
