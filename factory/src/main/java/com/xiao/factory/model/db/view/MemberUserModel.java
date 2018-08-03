package com.xiao.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.xiao.factory.model.db.AppDatabase;

/**
 * 群成员对应的用户的简单数据库表
 * Created by xiao on 2018/8/2.
 */

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    private String userId;
    @Column
    public String name;
    @Column
    public String portrait;
    @Column
    public String alias;
}
