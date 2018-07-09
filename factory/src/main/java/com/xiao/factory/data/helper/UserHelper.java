package com.xiao.factory.data.helper;


import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.user.UserUpdateModel;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHelper {

    public static void updateUserInfo(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {

        RemoteService service = Network.remote();

        Call<RspModel<UserCard>> call = service.updateUserInfo(model);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {

                RspModel<UserCard> rspModel = response.body();

                if (rspModel.success()) {

                    UserCard userCard = rspModel.getResult();

                    User user = userCard.build();

                    user.save();

                    callback.onDataLoadSuccess(userCard);
                } else {

                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {

            }
        });
    }

    /**
     * 搜索用户
     */
    public static Call<RspModel<List<UserCard>>> searchUser(String name,
                                                            final DataSource.Callback<List<UserCard>> callback) {

        RemoteService service = Network.remote();

        Call<RspModel<List<UserCard>>> call = service.searchUser(name);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {

                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoadSuccess(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {

                callback.onDataLoadFailed(R.string.data_network_error);
            }
        });

        return call;
    }


    // 关注的网络请求
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        Call<RspModel<UserCard>> call = service.userFollow(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    // 保存到本地数据库
                    User user = userCard.build();
                    user.save();
                    // TODO 通知联系人列表刷新

                    // 返回数据
                    callback.onDataLoadSuccess(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataLoadFailed(R.string.data_network_error);
            }
        });
    }

    /**
     * 刷新联系人的操作
     */
    public static void refreshContracts(final DataSource.Callback<List<UserCard>> callback) {

        RemoteService remoteService = Network.remote();

        remoteService.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {

                        RspModel<List<UserCard>> rspModel = response.body();
                        if (rspModel.success()) {

                            callback.onDataLoadSuccess(rspModel.getResult());
                        } else {

                            Factory.decodeRspCode(rspModel, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {

                        callback.onDataLoadFailed(R.string.data_network_error);

                    }
                });
    }

}
