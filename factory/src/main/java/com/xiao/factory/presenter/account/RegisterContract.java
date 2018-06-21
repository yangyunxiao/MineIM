package com.xiao.factory.presenter.account;


import com.xiao.common.factory.presenter.BaseContract;

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter> {

        void registerSuccess();

    }

    interface Presenter extends BaseContract.Presenter {

        void register(String phone, String name, String password);

        boolean checkMobile(String phone);

    }
}
