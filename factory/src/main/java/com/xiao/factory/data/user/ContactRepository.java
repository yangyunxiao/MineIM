package com.xiao.factory.data.user;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.model.db.User;
import com.xiao.factory.model.db.User_Table;
import com.xiao.factory.persisitence.Account;

import java.util.List;

/**
 * 联系人仓库
 */

public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {


    @Override
    public void load(SuccessCallback<List<User>> callback) {
        super.load(callback);

        //加载数据库中的数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }

}
