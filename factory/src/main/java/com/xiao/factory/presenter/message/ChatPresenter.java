package com.xiao.factory.presenter.message;

import com.xiao.factory.data.message.MessageDataSource;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.presenter.BaseSourcePresenter;

import java.util.List;

/**
 * 聊天Presenter的基础类
 */

public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {

    /**
     * 消息接收者的Id， 可能是群， 可能是人的ID
     */
    protected String mReceiverId;
    /**
     * 区分是人还是群
     */
    protected int mReceiverType;

    public ChatPresenter(MessageDataSource source, View view,String receiverId,int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;

    }

    @Override
    public void pushText(String content) {

    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {
        return false;
    }

    @Override
    public void onDataLoadSuccess(List<Message> messages) {

    }
}
