package com.xiao.mineim;

import android.app.Activity;
import android.os.Bundle;

import com.xiao.common.app.BaseActivity;
import com.xiao.mineim.fragment.assist.PermissionFragment;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断是否获取到全部权限  如果已经获取到  则跳转进入主界面  如果没有 则申请权限
        if (PermissionFragment.hasAllPermissions(this, getSupportFragmentManager())) {

            MainActivity.show(this);
            finish();
        }
    }
}
