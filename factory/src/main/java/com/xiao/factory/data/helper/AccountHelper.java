package com.xiao.factory.data.helper;

import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.R;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.db.User;

public class AccountHelper {

    /**
     * 注册的接口
     */
    public static void register(RegisterModel registerModel, final DataSource.Callback<User> callback) {


        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(3000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                callback.onDataLoadFailed(R.string.data_rsp_error_parameters);
            }
        }.start();

    }
}
