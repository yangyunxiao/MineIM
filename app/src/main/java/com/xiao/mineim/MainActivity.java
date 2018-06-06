package com.xiao.mineim;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.common.base.Objects;
import com.xiao.common.app.BaseActivity;
import com.xiao.common.widget.PortraitView;
import com.xiao.mineim.fragment.main.ActiveFragment;
import com.xiao.mineim.fragment.main.ContactFragment;
import com.xiao.mineim.fragment.main.GroupFragment;
import com.xiao.mineim.helper.NavigationHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationHelper.OnTabChangedListener<Integer> {


    private static final String TAG = "MAIN_ACTIVITY";

    private NavigationHelper<Integer> mNavigationHelper;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.main_portrait)
    PortraitView mPortrait;

    @BindView(R.id.main_text_title)
    TextView mTitle;

    @BindView(R.id.main_layout_container)
    FrameLayout mContainer;

    @BindView(R.id.main_button_action)
    FloatActionButton mFloatAction;

//    FloatActionButton m

    @BindView(R.id.main_navigation)
    BottomNavigationView mNavigation;

    @Override
    protected int getContentLayoutID() {

        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mNavigationHelper = new NavigationHelper<>(MainActivity.this,
                R.id.main_layout_container, getSupportFragmentManager(), this);

        mNavigation.setOnNavigationItemSelectedListener(this);

        mNavigationHelper.addTab(R.id.action_home, new NavigationHelper.Tab<Integer>(ActiveFragment.class, R.string.title_home))
                .addTab(R.id.action_contact, new NavigationHelper.Tab<Integer>(ContactFragment.class, R.string.title_contact))
                .addTab(R.id.action_group, new NavigationHelper.Tab<Integer>(GroupFragment.class, R.string.title_group));

        Glide.with(MainActivity.this)
                .load(R.mipmap.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<AppBarLayout, GlideDrawable>(mAppBarLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {

                        this.view.setBackground(resource);

                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();

//        mNavigationHelper.performClickMenu(R.id.action_home);

        //首次进入默认选中
        Menu menu = mNavigation.getMenu();

        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.main_image_search)
    void onSearchClick() {

    }


    /**
     * 底部导航栏选中回调
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        mNavigationHelper.performClickMenu(item.getItemId());

        return true;
    }

    @Override
    public void onTabChanged(NavigationHelper.Tab<Integer> newTab, NavigationHelper.Tab<Integer> oldTab) {

        mTitle.setText(newTab.mExtra);

        float translateY = 0;
        float rotation = 0;

        if (Objects.equal(newTab.mExtra, R.string.title_home)) {

            translateY = Ui.dipToPx(getResources(), 76);
        } else {

            if (Objects.equal(newTab.mExtra, R.string.title_contact)) {

                mFloatAction.setImageResource(R.drawable.ic_contact_add);
                rotation = -360;

            } else if (Objects.equal(newTab.mExtra, R.string.title_group)) {

                mFloatAction.setImageResource(R.drawable.ic_group_add);
                rotation = 360;

            }


        }

        mFloatAction.animate()
                .rotation(rotation)
                .translationY(translateY)
                .setInterpolator(new AnticipateOvershootInterpolator(1.0f))
                .setDuration(480)
                .start();


    }
}
