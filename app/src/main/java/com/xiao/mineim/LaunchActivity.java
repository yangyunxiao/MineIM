package com.xiao.mineim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.xiao.common.app.BaseActivity;
import com.xiao.factory.persisitence.Account;
import com.xiao.mineim.activity.AccountActivity;
import com.xiao.mineim.activity.MainActivity;
import com.xiao.mineim.fragment.assist.PermissionFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.activity_launch_root)
    View mRootView;

    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);

        ColorDrawable colorDrawable = new ColorDrawable(color);

        mRootView.setBackground(colorDrawable);

        mBgDrawable = colorDrawable;

    }

    @Override
    protected void initData() {
        super.initData();

        startColorAnim(.5f, new Runnable() {
            @Override
            public void run() {

                waitPushIdSet();
            }
        });
    }

    private void skipToMain() {
        //判断是否获取到全部权限  如果已经获取到  则跳转进入主界面  如果没有 则申请权限
        if (PermissionFragment.hasAllPermissions(this, getSupportFragmentManager())) {

            if (Account.isLogin()) {

                MainActivity.show(this);

            } else {
                AccountActivity.show(this);
            }
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

//        skipToMain();
    }

    private void waitPushIdSet() {

        if (Account.isLogin()) {

            if (Account.isBind()) {

                skipToMain();

                return ;
            }

        } else {

            if (!TextUtils.isEmpty(Account.getPushId())) {

                startColorAnim(1f, new Runnable() {
                    @Override
                    public void run() {
                        skipToMain();

                    }
                });

                return;

            }
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                waitPushIdSet();
            }
        }, 500);
    }

    private void startColorAnim(float endProgress, final Runnable endCallback) {

        int finalColor = Resource.Color.WHITE;

        ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        int endColor = (int) argbEvaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, argbEvaluator, endColor);
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });

        valueAnimator.start();

    }


    private final Property<LaunchActivity, Object> property
            = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LaunchActivity launchActivity) {
            return launchActivity.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {

            object.mBgDrawable.setColor((Integer) value);
        }
    };
}
