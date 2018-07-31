package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.factory.model.db.Session;
import com.xiao.factory.model.db.Session_Table;

/**
 * Created by xiao on 2018/7/31.
 * 会话辅助工具类
 */

class SessionHelper {
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
