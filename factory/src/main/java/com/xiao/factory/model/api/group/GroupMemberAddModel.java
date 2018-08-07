package com.xiao.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * 群成员添加Model
 * Created by xiao on 2018/8/2.
 */

public class GroupMemberAddModel {

    private Set<String> users = new HashSet<>();

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
