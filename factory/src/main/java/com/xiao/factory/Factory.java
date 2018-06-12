package com.xiao.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiao.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xiao on 2018/6/8.
 */

public class Factory {

    private static final Factory instance;

    private final Executor mExecutor;

    //全局的Gson
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //新建一个4线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .create();
    }


    public static void runOnAsync(Runnable runnable) {
        instance.mExecutor.execute(runnable);
    }


    public static Application application() {

        return Application.getInstance();
    }

    /**
     * 返回一个全局的Gson 这里可以添加gson初始化的配置条件
     */
    public static Gson getGson() {

        return instance.mGson;
    }
}
