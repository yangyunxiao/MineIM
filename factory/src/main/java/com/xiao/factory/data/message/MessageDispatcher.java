package com.xiao.factory.data.message;

import android.text.TextUtils;

import com.xiao.factory.data.helper.MessageHelper;
import com.xiao.factory.data.user.UserDispatcher;
import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.db.Message;

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


    public static MessageCenter instance(){
        if (instance == null){
            synchronized (UserDispatcher.class){
                if (instance == null){
                    instance = new MessageDispatcher();
                }
            }
        }

        return instance;
    }
    @Override
    public void dispatch(MessageCard... cards) {

        if (cards == null || cards.length == 0){
            return;

        }

        executor.execute(new MessageCardHandler(cards));
    }

    private class MessageCardHandler implements Runnable{

        private final MessageCard[] cards ;

        public MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {

            List<Message> messages = new ArrayList<>();

            for (MessageCard card : cards) {

                if (card == null || TextUtils.isEmpty(card.getSenderId())||TextUtils.isEmpty(card.getId())
                        ||(TextUtils.isEmpty(card.getReceiverId()) && TextUtils.isEmpty(card.getGroupId())))
                    continue;;

                Message message = MessageHelper.findFromLocal(card.getId());

                if (message != null){
                    if (message.getStatus() == Message.STATUS_DONE){
                        continue;
                    }

                    if (card.getStatus() == Message.STATUS_DONE){
                        message.setCreateAt(card.getCreateAt());
                    }
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                }


            }
        }
    }
}
