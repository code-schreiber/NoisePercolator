package com.toolslab.noisepercolator.view.messages

import com.toolslab.noisepercolator.db.DataProvider
import com.toolslab.noisepercolator.mvp.BasePresenter
import com.toolslab.noisepercolator.util.packagemanager.PackageManagerUtil
import io.reactivex.disposables.CompositeDisposable

class MessagesPresenter(private val packageManagerUtil: PackageManagerUtil = PackageManagerUtil(),
                        private val dataProvider: DataProvider = DataProvider(),
                        private val compositeDisposable: CompositeDisposable = CompositeDisposable())
    : BasePresenter<MessagesContract.View>(), MessagesContract.Presenter {

    override fun onBound(view: MessagesContract.View) {
        if (view.hasSmsPermission()) {
            initView(view)
        } else {
            onNoPermission()
        }
    }

    override fun onUnbound(view: MessagesContract.View) {
        compositeDisposable.clear()
    }

    override fun smsPermissionsGranted() {
        initView(view)
    }

    override fun onDefaultSmsAppButtonClicked() {
        packageManagerUtil.launchDefaultSmsApp()
    }

    override fun onNoPermission() {
        if (view.shouldShowRequestPermission()) {
            view.showPermissionExplanation()
        } else {
            // No explanation needed, we can request the permission.
            view.requestPermission()
        }
    }

    private fun initView(view: MessagesContract.View) {
        initDefaultSmsAppButtonText(view)

        compositeDisposable.add(dataProvider.getMessages()
                .subscribe {
                    // FIXME ASK This code runs twice on init
                    view.setInfoText(it.size)
                    view.initMessagesList(it.sorted())
                }
        )
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
