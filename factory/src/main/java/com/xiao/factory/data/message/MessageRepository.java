package com.xiao.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Message_Table;

import java.util.List;

/**
 * 跟某个人聊天的时候的聊天列表
 * 关注的内容一定是我发给这个人的，或者是他发送给我的消息
 */

public class MessageRepository extends BaseDbRepository<Message>
        implements MessageDataSource {

    /**
     * 聊天对象的ID
     */
    private String receiverId;

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {

        return (receiverId.equalsIgnoreCase(message.getSender().getId())
                && message.getGroup() == null) ||
                (message.getReceiver() != null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {

        super.onListQueryResult(transaction, tResult);

    }
}
