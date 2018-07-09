package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;

import com.xiao.common.app.BaseActivity;
import com.xiao.factory.model.db.User;
import com.xiao.mineim.R;

public class ChatActivity extends BaseActivity {

    public static void show(Context context, User user) {

        Intent chatIntent = new Intent(context, ChatActivity.class);
//        chatIntent.put("user", user);

        context.startActivity(chatIntent);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_chat;
    }
}
