package com.xiao.factory.model.api.message;


import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.persisitence.Account;

import java.util.Date;
import java.util.UUID;

public class MsgCreateModel {

    private String id;

    private String content;

    private String attach;

    private int type = Message.TYPE_STR;

    private String receiverId;

    private int receiverType = Message.RECEIVER_TYPE_NONE;

    private MsgCreateModel() {

        //随机生成一个UUID
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }


    public String getAttach() {
        return attach;
    }

    public int getType() {
        return type;
    }


    public String getReceiverId() {
        return receiverId;
    }


    public int getReceiverType() {
        return receiverType;
    }


    /**
     * 当我们需要发送一个文件的时候，content刷新的问题
     */
    private MessageCard card;

    public MessageCard buildCard() {

        if (card == null) {
            MessageCard card = new MessageCard();
            card.setId(id);
            card.setContent(content);
            card.setAttach(attach);
            card.setType(type);
            card.setSenderId(Account.getUserId());

            if (receiverType == Message.RECEIVER_TYPE_GROUP) {
                card.setGroupId(receiverId);
            } else {
                card.setReceiverId(receiverId);
            }

            //通过当前的model创建的Card就是一个初始状态的Card
            card.setStatus(Message.STATUS_CREATED);
            card.setCreateAt(new Date());
            this.card = card;
        }
        return this.card;
    }

    /**
     * 同步卡片的罪行状态
     */
    public void refreshByCard() {

        if (card == null) {
            return;
        }

        this.content = card.getContent();
        this.attach = card.getAttach();
    }

    /**
     * 建造者模式，快速的建立一个发送Model
     */
    public static class Builder {

        private MsgCreateModel model;

        public Builder() {
            this.model = new MsgCreateModel();
        }


        public Builder receiver(String receiverId, int receiverType) {
            this.model.receiverId = receiverId;
            this.model.receiverType = receiverType;
            return this;
        }

        public Builder content(String content, int type) {
            this.model.content = content;
            this.model.type = type;
            return this;
        }

        public Builder attach(String attach) {
            this.model.attach = attach;
            return this;
        }

        public MsgCreateModel build() {
            return this.model;
        }
    }


    /**
     * 把一个Message消息，转化成创建状态的CreateModel
     */
    public static MsgCreateModel buildWithMessage(Message message) {

        MsgCreateModel model = new MsgCreateModel();
        model.id = message.getId();
        model.content = message.getContent();
        model.type = message.getType();
        model.attach = message.getAttach();

        if (message.getReceiver() != null) {

            model.receiverId = message.getReceiver().getId();
            model.receiverType = Message.RECEIVER_TYPE_NONE;
        } else {
            model.receiverId = message.getGroup().getId();
            model.receiverType = Message.RECEIVER_TYPE_GROUP;
        }

        return model;
    }
}
