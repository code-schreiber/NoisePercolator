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
            onNoPermission()
        }
    }

    override fun smsPermissionsGranted() {
        initView(getView())
    }

    override fun onDefaultSmsAppButtonClicked() {
        packageManagerUtil.launchDefaultSmsApp()
    }

    override fun onNoPermission() {
        if (getView().shouldShowRequestPermission()) {
            getView().showPermissionExplanation()
        } else {
            // No explanation needed, we can request the permission.
            getView().requestPermission()
        }
    }

    private fun initView(view: MessagesContract.View) {
        val messages = dataProvider.getMessages()

        initDefaultSmsAppButtonText(view)
        view.setInfoText(messages.size)
        view.initMessagesList(messages.sorted())
    }

    private fun initDefaultSmsAppButtonText(view: MessagesContract.View) {
        val defaultSmsAppName = packageManagerUtil.getDefaultSmsAppName()
        if (defaultSmsAppName.isEmpty()) {
            view.setDefaultSmsAppButtonTextFallback()
        } else {
            view.setDefaultSmsAppButtonText(defaultSmsAppName)
        }
        view.setDefaultSmsAppButtonOnClickListener()
    }

}
