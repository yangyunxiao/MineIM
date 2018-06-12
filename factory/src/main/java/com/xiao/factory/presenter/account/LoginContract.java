package com.xiao.factory.presenter.account;

import com.xiao.common.factory.presenter.BaseContract;

public interface LoginContract {

    interface View extends BaseContract.View<Preserter> {

        void loginSuccess();

    }

    interface Preserter extends BaseContract.Preserter {

        void login(String phone, String name, String password);

    }
}
