package com.xiao.factory.presenter.message;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.User;
import com.xiao.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * 聊天契约
 */

public interface ChatContract {

    interface Presenter extends BaseContract.Presenter {

        void pushText(String content);

        void pushAudio(String path);

        void pushImages(String[] paths);

        /**
         * 重新发送消息，返回是否调度成功
         */
        boolean rePush(Message message);
    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {

        /**
         * 初始化的Model
         */
        void onInitTopPage(InitModel model);
    }

    /**
     * 联系人聊天界面
     */
    interface UserView extends View<User> {

    }

    /**
     * 群聊天的界面
     */
    interface GroupView extends View<Group> {

        /**
         * 显示管理员菜单
         */
        void showAdminOption(boolean isAdmin);

        /**
         * 初始化成员信息
         */
        void onInitGroupMembers(List<MemberUserModel> members, long moreCount);
    }
}
