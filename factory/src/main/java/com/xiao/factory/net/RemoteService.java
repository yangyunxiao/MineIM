package com.xiao.factory.net;

import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.api.account.LoginModel;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.api.user.UserUpdateModel;
import com.xiao.factory.model.card.UserCard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RemoteService {

    /**
     * 用户注册请求接口
     */
    @POST("account/register")
    Call<RspModel<AccountResponseModel>> accountRegister(@Body RegisterModel registerModel);

    /**
     * 登录
     */
    @POST("account/login")
    Call<RspModel<AccountResponseModel>> accountLogin(@Body LoginModel loginModel);

    /**
     * 绑定设备ID
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountResponseModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    /**
     * 用户更新
     */
    @PUT("user")
    Call<RspModel<UserCard>> updateUserInfo(@Body UserUpdateModel model);

}
