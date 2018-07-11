package com.xiao.factory.presenter.contact;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.User;

/**
 * 用户信息页面
 */

public interface PersonalContract {

    interface Presenter extends BaseContract.Presenter {

        //获取用户信息
        User getPersonalInfo();
    }

    interface View extends BaseContract.View<Presenter> {

        String getUserId();

        /**
         * 数据加载完毕
         */
        void onLoadDone(User user);

        /**
         * 是否允许发起聊天
         */
        void allowChat(boolean isAllow);

        /**
         * 设置关注的状态
         */
        void setFollowStatus(boolean isFollow);

    }
}
