package com.xiao.factory.net;

import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemoteService {

    /**
     * 用户注册请求接口
     */
    @POST
    Call<RspModel<AccountResponseModel>> accountRegister(@Body  RegisterModel registerModel);
}
