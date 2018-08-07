package com.xiao.factory.presenter.group;

import com.xiao.common.factory.presenter.BaseContract;

/**
 * 群成员添加的契约
 * Created by xiao on 2018/8/7.
 */

public interface GroupMemberAddContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 提交成员
         */
        void submit();

        /**
         * 更改一个Model的选中状态
         */
        void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected);
    }


    interface View extends BaseContract.RecyclerView<Presenter, GroupCreateContract.ViewModel> {

        /**
         * 添加群成员成功
         */
        void onAddedSucceed();

        /**
         * 获取群的Id
         */
        String getGroupId();
    }
}
