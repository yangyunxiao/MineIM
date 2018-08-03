package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.group.GroupCreateModel;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Group_Table;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;
import com.xiao.factory.presenter.group.GroupCreatePresenter;

import java.io.IOException;

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



}
