package com.xiao.factory.presenter.message;

import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.data.message.MessageGroupRepository;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.persisitence.Account;

import java.util.List;

/**
 * 群聊天的逻辑层
 * Created by xiao on 2018/8/6.
 */

public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {
    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();

        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {

            ChatContract.GroupView view = getView();

            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);

            //基础信息初始化
            view.onInitTopPage(group);

            List<MemberUserModel> models = group.getLatelyGroupMembers();
            final long memberCount = group.getGroupMemberCount();

            //没有显示的成员的数量
            long moreCount = memberCount - models.size();
            view.onInitGroupMembers(models, moreCount);
        }

    }
}
