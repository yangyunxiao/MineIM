package com.xiao.factory.model.db;


import com.google.common.base.Objects;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.model.Author;
import com.xiao.factory.utils.DiffUiDataCallback;

import java.util.Date;

@Table(database = AppDatabase.class)
public class User extends BaseDbModel<User> implements Author {

    public static final int SEX_MAN = 1;

    public static final int SEX_WOMAN = 2;

    @PrimaryKey
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String portrait;
    @Column
    private String desc;
    @Column
    private int sex = 0;
    @Column
    private String alias;
    @Column
    private int follows;
    @Column
    private int following;
    @Column
    private boolean follow;
//    @Column
//    private Date modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    @Override
    public int hashCode() {

        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean isSame(User old) {
        return this == old || Objects.equal(id, old.id);
    }

    @Override
    public boolean isUiContentSame(User old) {
        return this == old || (
                Objects.equal(name, old.name) && Objects.equal(portrait, old.portrait)
                        && Objects.equal(sex, old.sex) && Objects.equal(follow, old.isFollow())
        );
    }

//    public Date getModifyAt() {
//        return modifyAt;
//    }
//
//    public void setModifyAt(Date modifyAt) {
//        this.modifyAt = modifyAt;
//    }
}
