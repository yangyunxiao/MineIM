package com.xiao.factory.presenter.user;

import com.xiao.common.factory.presenter.BaseContract;

public interface UpdateInfoContract {


    interface Presenter extends BaseContract.Presenter {

        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {

        void updateSucceed();
    }
}
