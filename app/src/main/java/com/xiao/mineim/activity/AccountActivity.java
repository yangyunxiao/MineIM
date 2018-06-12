package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xiao.common.app.BaseActivity;
import com.xiao.common.app.BaseFragment;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.account.IAccountTrigger;
import com.xiao.mineim.fragment.account.LoginFragment;
import com.xiao.mineim.fragment.account.RegisterFragment;
import com.xiao.mineim.fragment.account.UpdateInfoFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements IAccountTrigger {

    /**
     * 当前正在显示的Fragment
     */
    private Fragment mCurrentFragment;

    private Fragment mLoginFragment;

    private Fragment mRegisterFragment;

    @BindView(R.id.account_image_bg)
    ImageView mBackground;

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

        mLoginFragment = new LoginFragment();

        mCurrentFragment = mLoginFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.account_frame_container, mCurrentFragment)
                .commit();

        Glide.with(this)
                .load(R.mipmap.bg_src_tianjin)
                .centerCrop()
                .into(new ViewTarget<ImageView, GlideDrawable>(mBackground) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                        Drawable drawable = resource.getCurrent();

                        drawable = DrawableCompat.wrap(drawable);

                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);

                        this.view.setImageDrawable(drawable);

                    }
                });
    }


    @Override
    public void triggerView() {

        if (mCurrentFragment == mLoginFragment) {

            if (mRegisterFragment == null) {

                mRegisterFragment = new RegisterFragment();
            }

            mCurrentFragment = mRegisterFragment;

        } else {

            mCurrentFragment = mLoginFragment;
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.account_frame_container, mCurrentFragment)
                .commit();
    }
}
