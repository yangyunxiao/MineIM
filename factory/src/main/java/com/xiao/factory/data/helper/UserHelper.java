package com.xiao.factory.data.helper;


import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.user.UserUpdateModel;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;

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
}
