package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.xiao.common.app.BaseActivity;
import com.xiao.common.app.BaseFragment;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;

public class AccountActivity extends BaseActivity {

    private BaseFragment mUpdateInfoFragment;

    @BindView(R.id.account_frame_container)
    FrameLayout mContainerLayout;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_account;
    }

    public static void show(Context context) {

        context.startActivity(new Intent(context, AccountActivity.class));

    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mUpdateInfoFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.account_frame_container, mUpdateInfoFragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        mUpdateInfoFragment.onActivityResult(requestCode,resultCode,data);
    }

}
