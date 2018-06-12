package com.xiao.factory.presenter;

import android.view.View;

import com.xiao.common.factory.presenter.BaseContract;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Preserter {

    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    protected final T getView() {
        return mView;
    }

    public void setView(T mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

        if (mView != null) {

            mView.showLoading();
        }
    }


    @Override
    public void destroy() {

        T view = mView;

        mView = null;

        if (view != null) {

            view.setPresenter(null);
        }
    }
}
