package com.xiao.mineim;

import com.igexin.sdk.PushManager;
import com.xiao.common.app.Application;
import com.xiao.factory.Factory;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Factory.setup();

        PushManager.getInstance().initialize(this, null);
    }
}
