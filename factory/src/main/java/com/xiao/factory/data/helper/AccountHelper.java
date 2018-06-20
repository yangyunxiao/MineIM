package com.xiao.factory.data.helper;

import android.text.TextUtils;

import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.api.account.LoginModel;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.db.User;
import com.xiao.factory.net.Network;
import com.xiao.factory.net.RemoteService;
import com.xiao.factory.persisitence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {

    /**
     * 注册的接口
     */
    public static void register(final RegisterModel registerModel, final DataSource.Callback<User> callback) {

        RemoteService remoteService = Network.remote();

        Call<RspModel<AccountResponseModel>> call = remoteService.accountRegister(registerModel);

        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 绑定用户设备ID
     */
    public static void bindPush(DataSource.Callback<User> callback) {

        String pushId = Account.getPushId();

        if (!TextUtils.isEmpty(pushId)) {

            return;

        }

        RemoteService remoteService = Network.remote();

        Call<RspModel<AccountResponseModel>> call = remoteService.accountBind(pushId);

        call.enqueue(new AccountRspCallback(callback));

    }

    /**
     * 登录
     */
    public static void login(LoginModel loginModel, DataSource.Callback<User> callback) {

        RemoteService remoteService = Network.remote();

        Call<RspModel<AccountResponseModel>> call = remoteService.accountLogin(loginModel);

        call.enqueue(new AccountRspCallback(callback));

    }


    private static class AccountRspCallback implements Callback<RspModel<AccountResponseModel>> {

        private DataSource.Callback<User> callback;

        public AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountResponseModel>> call, Response<RspModel<AccountResponseModel>> response) {

            //请求成功返回
            //从返回中得到我们的全局Model，内部是使用Gson进行解析
            RspModel<AccountResponseModel> rspModel = response.body();

            if (rspModel.success()) {
                // 拿到实体
                AccountResponseModel accountResponseModel = rspModel.getResult();

                User user = accountResponseModel.getUser();

                user.save();

                Account.login(accountResponseModel);
                //是否已绑定设备
                if (accountResponseModel.isBind()) {

                    Account.setBind(true);

                    //进行的是数据库写入和缓存绑定 然后返回
                    callback.onDataLoadSuccess(user);

                } else {

                    bindPush(callback);

                }
            } else {

                Factory.decodeRspCode(rspModel, callback);
            }
        }

        @Override
        public void onFailure(Call<RspModel<AccountResponseModel>> call, Throwable throwable) {

            callback.onDataLoadFailed(R.string.data_network_error);

        }
    }
}
