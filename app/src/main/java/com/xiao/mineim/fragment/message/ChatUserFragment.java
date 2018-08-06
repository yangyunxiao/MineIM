package com.xiao.mineim.fragment.message;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.common.widget.PortraitView;
import com.xiao.factory.model.db.User;
import com.xiao.factory.presenter.message.ChatContract;
import com.xiao.factory.presenter.message.ChatUserPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户聊天的界面
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {


    @BindView(R.id.chat_portrait)
    PortraitView mPortrait;

    private MenuItem mUserInfoMenuItem;

    public ChatUserFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getHeaderLayoutId() {
        return R.layout.layout_chat_header_user;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        Glide.with(this)
                .load(R.mipmap.default_banner_chat)
                .centerCrop()
                .into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(mCollapsingToolbarLayout) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setContentScrim(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onPortraitClick();
                }
                return false;
            }
        });

        //拿到菜单的Icon
        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);
    }

    /**
     * 进行高度的综合运算，透明头像和Icon
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);

        View view = mPortrait;
        MenuItem menuItem = mUserInfoMenuItem;

        if (view == null || menuItem == null)
            return;

        if (verticalOffset == 0) {

            //完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

            //隐藏菜单
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {

            //abs 运算
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {

                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);

                //显示菜单
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {

                //中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

                //和头像恰好相反
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255 - (int) (255 * progress));
            }

        }

    }

    @OnClick(R.id.chat_portrait)
    void onPortraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);
    }

    @Override
    public void onInitTopPage(User user) {

        mPortrait.setup(Glide.with(this), user.getPortrait());
        mCollapsingToolbarLayout.setTitle(user.getName());
    }

}
