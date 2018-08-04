package com.xiao.mineim;

import android.app.Activity;

import com.igexin.sdk.PushManager;
import com.xiao.common.app.Application;
import com.xiao.factory.Factory;
import com.xiao.mineim.activity.AccountActivity;

import java.util.List;

//import tech.linjiang.pandora.Pandora;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //调用Factory进行初始化
        Factory.setup();

        //推送进行初始化
        PushManager.getInstance().initialize(this, null);

//        Pandora.init(this);
//
//        Pandora.get().open();
    }

    @Override
    public void logout() {
        super.logout();
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        AccountActivity.show(this);
    }
}
