package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.factory.Factory;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.message.MsgCreateModel;
import com.xiao.factory.model.card.MessageCard;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Message_Table;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息处理工具类
 */

public class MessageHelper {

    /**
     * 从本地查询消息
     */
    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void push(final MsgCreateModel model) {

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                Message message = findFromLocal(model.getId());

                //成功状态，如果是一个已经发送过得消息，则不能重新发送
                //正在发送的状态： 如果是一个消息正在发送，则不能重新发送
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {

                    return;
                }

                //TODO 如果是文件类型的消息，则需要先上传在发送
                final MessageCard card = model.buildCard();
                Factory.getMessageCenter().dispatch(card);

                //直接发送， 进行网络调度
                RemoteService service = Network.remote();
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {

                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {

                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);
                            }
                        } else {
                            //检查是否是账户异常
                            Factory.decodeRspCode(rspModel, null);
                            //走失败的流程
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {

                        //通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    /**
     * 查询一个消息，这个消息是一个群中的最后一条信息
     */
    public static Message findLastWithGroup(String groupId) {

        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(groupId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }

    public static Message findLastWithUser(String userId) {
        return SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(userId)).and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId))
                .orderBy(Message_Table.createAt, false)
                .querySingle();
    }
}
