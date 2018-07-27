package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.factory.model.api.message.MsgCreateModel;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Message_Table;

/**
 * 消息处理工具类
 */

public class MessageHelper {

    public static Message findFromLocal(String id) {
        return SQLite.select()
                .from(Message.class)
                .where(Message_Table.id.eq(id))
                .querySingle();
    }

    public static void push(final MsgCreateModel model){

    }

}
