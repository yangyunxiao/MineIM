package com.xiao.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BaseContract;

public abstract class ViewFragment<Presenter extends BaseContract.Presenter>
        extends BaseFragment implements BaseContract.View<Presenter> {

    protected Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        initPresenter();

    }

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

    @Override
    public void setPresenter(Presenter presenter) {

        mPresenter = presenter;

    }

}
