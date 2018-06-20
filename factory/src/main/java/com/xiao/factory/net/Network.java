package com.xiao.factory.net;

import android.text.TextUtils;

import com.xiao.common.Common;
import com.xiao.factory.Factory;
import com.xiao.factory.persisitence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    public static Network instance;

    private Retrofit retrofit;

    static {

        instance = new Network();
    }

    private Network() {
    }

    public static Retrofit getRetrofit() {

        if (instance.retrofit != null) {

            return instance.retrofit;
        }

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                String token = Account.getToken();
                if (!TextUtils.isEmpty(token)) {
                    builder.addHeader("token", token);

                }
                builder.addHeader("Content-Type", "Application/json");

                Request newRequest = builder.build();

                return chain.proceed(newRequest);
            }
        };

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();

        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;

    }

    public static RemoteService remote() {

        return Network.getRetrofit().create(RemoteService.class);
    }
}
