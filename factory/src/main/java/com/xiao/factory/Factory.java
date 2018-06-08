package com.xiao.factory;

import com.xiao.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xiao on 2018/6/8.
 */

public class Factory {

    private static final Factory instance;

    private final Executor mExecutor;

    static {
        instance = new Factory();
    }

    private Factory() {
        //新建一个4线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);
    }


    public static void runOnAsync(Runnable runnable) {
        instance.mExecutor.execute(runnable);
    }


    public static Application application() {

        return Application.getInstance();
    }
}
