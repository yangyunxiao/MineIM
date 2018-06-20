package com.xiao.mineim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.xiao.factory.Factory;
import com.xiao.factory.data.helper.AccountHelper;
import com.xiao.factory.persisitence.Account;

public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) {

            return;
        }

        Bundle bundle = intent.getExtras();

        //判断此消息的意图
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {

            case PushConsts.GET_CLIENTID:

                Log.i(TAG, "GET_CLIENTID " + bundle.toString());

                //pushID初始化
                onClientInit(bundle.getString("clientid"));
                break;

            case PushConsts.GET_MSG_DATA:

                byte[] payload = bundle.getByteArray("payload");

                if (payload != null) {

                    String message = new String(payload);

                    onMessageArrived(message);

                }

                break;

            default:
                break;
        }

    }

    /**
     * 消息送达时
     */
    private void onMessageArrived(String message) {

        //交给Factory分发消息
        Factory.dispatchPush(message);

    }

    private void onClientInit(String clientId) {

        Account.setPushId(clientId);

        if (Account.isLogin()) {

            //账户已经登录  进行pushid绑定操作
            AccountHelper.bindPush(null);
        }

    }
}
