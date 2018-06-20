package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xiao.common.app.BaseActivity;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.factory.data.DataSource;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.account.UpdateInfoFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class UserActivity extends BaseActivity {

    private BaseFragment mUpdateInfoFragment;


    @BindView(R.id.user_frame_container)
    FrameLayout mContainerLayout;

    @BindView(R.id.user_image_bg)
    ImageView mBackground;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_user;
    }

    public static void show(Context context) {

        context.startActivity(new Intent(context, UserActivity.class));

    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mUpdateInfoFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.user_frame_container, mUpdateInfoFragment)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        mUpdateInfoFragment.onActivityResult(requestCode, resultCode, data);

    }
}
