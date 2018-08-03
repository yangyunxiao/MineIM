package com.xiao.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Group_Table;
import com.xiao.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * 群组数据仓库是对GroupDataSource的实现
 * Created by xiao on 2018/8/3.
 */

public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupDataSource {

    @Override
    public void load(SuccessCallback<List<Group>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {

        if (group.getGroupMemberCount() > 0){
            group.holder = buildGroupHolder(group);
        }else{
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        return true;
    }

    /**
     * 初始化界面显示的成员信息
     */
    private String buildGroupHolder(Group group) {

        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0){
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels){
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "),builder.length());
        return builder.toString();
    }
}
