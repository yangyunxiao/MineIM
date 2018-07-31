package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.xiao.common.app.BaseActivity;
import com.xiao.common.factory.model.Author;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.Session;
import com.xiao.factory.model.db.User;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.message.ChatGroupFragment;
import com.xiao.mineim.fragment.message.ChatUserFragment;

import org.w3c.dom.Text;

public class ChatActivity extends BaseActivity {

    public final static String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    private final static String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;


    public static void show(Context context, Session session) {

        if (session == null || context == null || TextUtils.isEmpty(session.getId())) {
            return;
        }
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    /**
     * 打开个人聊天页面
     */
    public static void show(Context context, Author user) {

        if (user == null || context == null || TextUtils.isEmpty(user.getId())) {
            return;
        }

        Intent chatIntent = new Intent(context, ChatActivity.class);

        chatIntent.putExtra(KEY_RECEIVER_ID, user.getId());
        chatIntent.putExtra(KEY_RECEIVER_IS_GROUP, false);

        context.startActivity(chatIntent);
    }

    /**
     * 打开群聊天页面
     */
    public static void show(Context context, Group group) {

        if (group == null || context == null || TextUtils.isEmpty(group.getId())) {
            return;
        }
        Intent groupIntent = new Intent(context, ChatActivity.class);
        groupIntent.putExtra(KEY_RECEIVER_ID, group.getId());
        groupIntent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(groupIntent);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_chat;
    }

    @Override
    protected boolean initArgs(Bundle extras) {
        mReceiverId = extras.getString(KEY_RECEIVER_ID);
        mIsGroup = extras.getBoolean(KEY_RECEIVER_IS_GROUP, true);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, fragment).commit();
    }
}
