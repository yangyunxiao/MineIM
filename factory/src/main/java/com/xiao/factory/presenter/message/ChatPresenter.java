package com.xiao.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.xiao.factory.data.helper.MessageHelper;
import com.xiao.factory.data.message.MessageDataSource;
import com.xiao.factory.model.api.message.MsgCreateModel;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.presenter.BaseSourcePresenter;
import com.xiao.factory.utils.DiffUiDataCallback;

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

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;

    }

    @Override
    public void pushText(String content) {

        //构建一个新消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();

        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushImages(String[] paths) {

    }

    @Override
    public boolean rePush(Message message) {

        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {

            message.setStatus(Message.STATUS_CREATED);

            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }

        return false;
    }

    @Override
    public void onDataLoadSuccess(List<Message> messages) {

        ChatContract.View view = getView();
        if (view == null) {
            return;
        }

        List<Message> oldMessage = view.getRecyclerAdapter().getItems();

        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(oldMessage, messages);

        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //进行界面刷新
        refreshData(result, messages);
    }
}
