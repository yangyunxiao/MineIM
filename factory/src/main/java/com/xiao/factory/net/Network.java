package com.xiao.factory.net;

import com.xiao.common.Common;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    public static Retrofit getRetrofit() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit.Builder builder = new Retrofit.Builder();

        Retrofit retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }
}
