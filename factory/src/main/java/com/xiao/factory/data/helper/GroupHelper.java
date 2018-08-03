package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.group.GroupCreateModel;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.card.GroupMemberCard;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.GroupMember;
import com.xiao.factory.model.db.GroupMember_Table;
import com.xiao.factory.model.db.Group_Table;
import com.xiao.factory.model.db.User;
import com.xiao.factory.model.db.User_Table;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;
import com.xiao.factory.presenter.group.GroupCreatePresenter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 对群的一个简单的辅助工具类
 */

public class GroupHelper {


    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null) {

            group = findFromNet(groupId);
        }

        return group;

    }

    private static Group findFromNet(String groupId) {

        RemoteService remoteService = Network.remote();

        try {
            Response<RspModel<GroupCard>> response = remoteService.groupFind(groupId).execute();

            GroupCard card = response.body().getResult();

            if (card != null) {
                Factory.getGroupCenter().dispatch(card);
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Group findFromLocal(String groupId) {
        //本地查找群信息
        return SQLite.select()
                .from(Group.class)
                .where(Group_Table.id.eq(groupId))
                .querySingle();

    }

    /**
     * 群的创建
     */
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {

        RemoteService service = Network.remote();

        service.groupCreate(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {

                RspModel<GroupCard> rspModel = response.body();
                if (rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();

                    Factory.getGroupCenter().dispatch(groupCard);

                    //返回数据
                    callback.onDataLoadSuccess(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {

                callback.onDataLoadFailed(R.string.data_network_error);
            }
        });
    }


    /**
     * 刷新我的群组列表
     */
    public static void refreshGroups() {

        RemoteService service = Network.remote();

        service.groups("").enqueue(new Callback<RspModel<List<GroupCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupCard>>> call, Response<RspModel<List<GroupCard>>> response) {

                RspModel<List<GroupCard>> rspModel = response.body();

                if (rspModel.success()) {

                    List<GroupCard> groupCards = rspModel.getResult();
                    if (groupCards != null && groupCards.size() > 0) {
                        Factory.getGroupCenter().dispatch(groupCards.toArray(new GroupCard[0]));
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupCard>>> call, Throwable t) {

                // 不做任何处理
            }
        });

    }

    public static long getMemberCount(String id) {

        return SQLite.selectCountOf()
                .from(GroupMember.class)
                .where(GroupMember_Table.group_id.eq(id))
                .count();
    }

    /**
     * 网络刷新群的成员信息
     */
    public static void refreshGroupMember(Group group) {
        RemoteService service = Network.remote();

        service.groupMembers(group.getId()).enqueue(new Callback<RspModel<List<GroupMemberCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<GroupMemberCard>>> call, Response<RspModel<List<GroupMemberCard>>> response) {
                RspModel<List<GroupMemberCard>> rspModel = response.body();

                if (rspModel.success()) {
                    List<GroupMemberCard> memberCards = rspModel.getResult();

                    if (memberCards != null && memberCards.size() > 0) {

                        Factory.getGroupCenter().dispatch(memberCards.toArray(new GroupMemberCard[0]));
                    }
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<GroupMemberCard>>> call, Throwable t) {

            }
        });
    }


    /**
     * 关联查询一个用户和群成员的表，返回一个MemberUserModel表的集合
     */
    public static List<MemberUserModel> getMemberUsers(String groupId, int size) {

        return SQLite.select(GroupMember_Table.alias.withTable().as("alias"),
                User_Table.id.withTable().as("id"),
                User_Table.name.withTable().as("name"),
                User_Table.portrait.withTable().as("portrait"))
                .from(GroupMember.class)
                .join(User.class, Join.JoinType.INNER)
                .on(GroupMember_Table.user_id.withTable().eq(User_Table.id.withTable()))
                .where(GroupMember_Table.group_id.withTable().eq(groupId))
                .orderBy(GroupMember_Table.user_id, true)
                .limit(size)
                .queryCustomList(MemberUserModel.class);
    }
}
