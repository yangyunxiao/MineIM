package com.xiao.mineim;

import com.igexin.sdk.PushManager;
import com.xiao.common.app.Application;
import com.xiao.factory.Factory;

import tech.linjiang.pandora.Pandora;


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
}
