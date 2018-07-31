package com.xiao.factory.data.message;

import android.text.TextUtils;

import com.xiao.factory.data.helper.DbHelper;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.data.helper.MessageHelper;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.data.user.UserDispatcher;
import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 消息中心的实现类
 */

public class MessageDispatcher implements MessageCenter {

    private static MessageCenter instance;
    private final Executor executor = Executors.newSingleThreadExecutor();


    public static MessageCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }

        return instance;
    }

    @Override
    public void dispatch(MessageCard... cards) {

        if (cards == null || cards.length == 0) {

            return;

        }

        executor.execute(new MessageCardHandler(cards));

    }

    private class MessageCardHandler implements Runnable {

        private final MessageCard[] cards;

        public MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

            List<Message> messages = new ArrayList<>();

            for (MessageCard card : cards) {

                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId())))
                    continue;

                // 消息卡片可能是推送过来的，也有可能是直接造的，
                //服务器推送过来的代表服务器一定有，我们可以查询得到，
                //如果是本地造的，那么先存储本地，然后发送网络
                //发送消息的流程：些消息->存储消息->发送网络->网络返回->刷新本地状态
                Message message = MessageHelper.findFromLocal(card.getId());

                if (message != null) {

                    if (message.getStatus() == Message.STATUS_DONE) {
                        continue;
                    }

                    if (card.getStatus() == Message.STATUS_DONE) {
                        message.setCreateAt(card.getCreateAt());
                    }

                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {

                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());

                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }

                    if (receiver == null && group == null && sender != null) {
                        continue;
                    }

                    message = card.build(sender, receiver, group);
                }

                messages.add(message);

            }

            if (messages.size() > 0) {

                DbHelper.save(Message.class, messages.toArray(new Message[0]));
            }
        }
    }
}
