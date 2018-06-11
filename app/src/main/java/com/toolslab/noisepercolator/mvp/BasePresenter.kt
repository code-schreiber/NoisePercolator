package com.toolslab.noisepercolator.mvp

abstract class BasePresenter<V : BaseView> : MvpPresenter<V> {

    protected lateinit var view: V

    override fun bind(view: V) {
        this.view = view
        onBound(view)
    }

    override fun onBound(view: V) {
        // For use in subclasses
    }

    override fun unbind(view: V) {
        onUnbound(view)
    }

    override fun onUnbound(view: V) {
        // For use in subclasses
    }

}
