package com.xiao.common.factory.presenter;

import android.support.annotation.StringRes;

public interface BaseContract {

    interface View<T extends Preserter> {

        void showError(@StringRes int error);

        void showLoading();

        void setPresenter(T presenter);

    }

    interface Preserter {

        void start();

        void destroy();

    }
}
