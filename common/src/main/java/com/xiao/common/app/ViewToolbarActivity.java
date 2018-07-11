package com.xiao.common.app;

import android.support.annotation.StringRes;

import com.xiao.common.factory.presenter.BaseContract;

/**
 * View Activity 封装基类
 */


public abstract class ViewToolbarActivity<Presenter extends BaseContract.Presenter>
        extends ToolbarActivity implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;


    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    /**
     * 初始化Presenter
     */
    protected abstract Presenter initPresenter();

    @Override
    public void showError(@StringRes int error) {

        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(error);
        } else {
            Application.showToast(error);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    protected void hideLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }


    @Override
    public void setPresenter(Presenter presenter) {

        //View中赋值Presenter
        mPresenter = presenter;

    }

}
