package com.xiao.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.model.db.Session;
import com.xiao.factory.model.db.Session_Table;

import java.util.Collections;
import java.util.List;

/**
 * Created by xiao on 2018/7/31.
 * 会话列表
 */

public class SessionRepository extends BaseDbRepository<Session>
        implements SessionDataSource {

    @Override
    public void load(SuccessCallback<List<Session>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Session.class)
                .orderBy(Session_Table.modifyAt, false)//false是倒序
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }


    @Override
    protected boolean isRequired(Session session) {
        return true;
    }

    @Override
    protected void insert(Session session) {
        dataList.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        //复写数据库回来的数据，进行反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}

