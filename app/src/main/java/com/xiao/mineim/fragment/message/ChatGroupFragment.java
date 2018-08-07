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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xiao.common.app.BaseFragment;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.GroupMember;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.presenter.message.ChatContract;
import com.xiao.factory.presenter.message.ChatGroupPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.GroupMemberActivity;
import com.xiao.mineim.activity.PersonalActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 群聊天页面
 */
public class ChatGroupFragment extends ChatFragment<Group>
        implements ChatContract.GroupView {

    @BindView(R.id.im_header)
    ImageView mHeader;

    @BindView(R.id.lay_members)
    LinearLayout mLayMembers;

    @BindView(R.id.txt_member_more)
    TextView mMemberMore;


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.layout_chat_header_group;
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
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    public void onInitTopPage(Group group) {

        mCollapsingToolbarLayout.setTitle(group.getName());
        Glide.with(this)
                .load(group.getPicture())
                .centerCrop()
                .placeholder(R.mipmap.default_banner_group)
                .into(mHeader);
    }

    @Override
    public void showAdminOption(boolean isAdmin) {

        if (isAdmin) {
            mToolbar.inflateMenu(R.menu.chat_group);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {
                        GroupMemberActivity.showAdmin(getContext(), mReceiverId);
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {

        if (members == null || members.size() == 0) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final MemberUserModel member : members) {

            ImageView portrait = (ImageView) inflater.inflate(R.layout.lay_chat_group_portrait, mLayMembers, false);
            mLayMembers.addView(portrait, 0);

            Glide.with(this)
                    .load(member.portrait)
                    .placeholder(R.mipmap.default_portrait)
                    .centerCrop()
                    .dontAnimate()
                    .into(portrait);

            portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonalActivity.show(getContext(), member.userId);
                }
            });

            if (moreCount > 0) {
                mMemberMore.setText(String.format("+%s", moreCount));
                mMemberMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupMemberActivity.show(getContext(), mReceiverId);
                    }
                });
            } else {
                mMemberMore.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 进行高度的综合运算，透明头像和Icon
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);

        View view = mLayMembers;

        if (view == null)
            return;

        if (verticalOffset == 0) {

            //完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

        } else {

            //abs 运算
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {

                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);

            } else {

                //中间状态
                float progress = 1 - verticalOffset / (float) totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

            }

        }

    }
}
