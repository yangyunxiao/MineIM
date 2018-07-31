package com.xiao.factory.presenter.message;

import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.data.message.MessageRepository;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.User;

/**
 * 用户聊天的Presenter层
 */

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();

        User receiver = UserHelper.searchUserFromLocal(mReceiverId);
        getView().onInitTopPage(receiver);
    }
}
