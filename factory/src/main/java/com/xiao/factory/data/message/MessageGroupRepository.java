package com.xiao.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Message_Table;
import com.xiao.factory.persisitence.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 跟群聊天的时候的聊天列表
 * 关注的内容一定是我发给群的或者别人发送到群的消息
 * <p>
 * Created by xiao on 2018/8/6.
 */

public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {

    /**
     * 聊天群的Id
     */
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return message.getGroup() != null
                && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
