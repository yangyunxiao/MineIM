package com.xiao.factory.data.helper;

import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.R;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {

    /**
     * 注册的接口
     */
    public static void register(final RegisterModel registerModel, final DataSource.Callback<User> callback) {

        RemoteService remoteService = Network.getRetrofit().create(RemoteService.class);

        Call<RspModel<AccountResponseModel>> call = remoteService.accountRegister(registerModel);

        call.enqueue(new Callback<RspModel<AccountResponseModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountResponseModel>> call, Response<RspModel<AccountResponseModel>> response) {

                //请求成功返回
                //从返回中得到我们的全局Model，内部是使用Gson进行解析
                RspModel<AccountResponseModel> rspModel = response.body();

                if (rspModel.success()){
                    // 拿到实体
                    AccountResponseModel accountResponseModel = rspModel.getResult();
                    //是否已绑定设备
                    if (accountResponseModel.isBind()){
                        User user = accountResponseModel.getUser();
                        //进行的是数据库写入和缓存绑定 然后返回
                        callback.onDataLoadSuccess(user);
                    }else{

                        bindPush(callback);
                    }
                }else{

//                    callback.on
                }

            }

            @Override
            public void onFailure(Call<RspModel<AccountResponseModel>> call, Throwable throwable) {

                callback.onDataLoadFailed(R.string.data_network_error);
            }
        });
    }

    public static void bindPush(DataSource.Callback<User> callback){

    }
}
