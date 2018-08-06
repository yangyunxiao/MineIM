package com.xiao.factory.presenter.group;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.view.MemberUserModel;

/**
 * 群成员的契约
 * Created by xiao on 2018/8/6.
 */

public interface GroupMemberContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 刷新方法
         */
        void refresh();
    }

    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {

        /**
         * 获取群Id
         */
        String getGroupId();
    }
}
