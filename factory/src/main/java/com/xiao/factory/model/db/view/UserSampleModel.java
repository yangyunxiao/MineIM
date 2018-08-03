package com.xiao.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.xiao.common.factory.model.Author;
import com.xiao.factory.model.db.AppDatabase;

/**
 * 用户基础信息的Model 可以和数据可进行查询
 * Created by xiao on 2018/8/2.
 */
@QueryModel(database = AppDatabase.class)
public class UserSampleModel implements Author {
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String portrait;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPortrait() {
        return portrait;
    }

    @Override
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
