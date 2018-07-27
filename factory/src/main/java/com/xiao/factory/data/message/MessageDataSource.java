package com.xiao.factory.data.message;

import com.xiao.common.factory.data.DbDataSource;
import com.xiao.factory.model.db.Message;

/**
 * 消息的数据源定义
 * 关注的对象是Message表
 */

public interface MessageDataSource extends DbDataSource<Message> {
}
