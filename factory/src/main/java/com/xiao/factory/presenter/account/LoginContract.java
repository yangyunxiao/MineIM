package com.xiao.factory.presenter.account;

import com.xiao.common.factory.presenter.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View<Presenter> {

        void loginSuccess();

    }

    interface Presenter extends BaseContract.Preserter {

        void login(String phone, String password);

    }
}
