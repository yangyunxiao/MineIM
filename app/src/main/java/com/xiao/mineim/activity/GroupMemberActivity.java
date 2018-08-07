package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.ViewToolbarActivity;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.presenter.group.GroupMemberContract;
import com.xiao.factory.presenter.group.GroupMemberPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.group.GroupMemberAddFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends ViewToolbarActivity<GroupMemberContract.Presenter>
        implements GroupMemberContract.View, GroupMemberAddFragment.Callback {

    private static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    private static final String KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN";

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private String mGroupId;
    private boolean mIsAdmin;
    private RecyclerAdapter<MemberUserModel> mAdapter;


    public static void show(Context context, String groupId) {

        show(context, groupId, false);
    }

    public static void showAdmin(Context context, String groupId) {

        show(context, groupId, true);

    }

    private static void show(Context context, String groupId, boolean isAdmin) {

        if (TextUtils.isEmpty(groupId)) {
            return;
        }

        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_ADMIN, isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_group_member;
    }

    @Override
    protected boolean initArgs(Bundle extras) {
        mGroupId = extras.getString(KEY_GROUP_ID);
        mIsAdmin = extras.getBoolean(KEY_GROUP_ADMIN);
        return !TextUtils.isEmpty(mGroupId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(R.string.title_member_list);

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            public int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.refresh();
        //显示管理员界面 添加成员
        if (mIsAdmin) {
            new GroupMemberAddFragment().
                    show(getSupportFragmentManager(), GroupMemberAddFragment.class.getName());
        }
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    @Override
    protected GroupMemberContract.Presenter initPresenter() {
        return new GroupMemberPresenter(this);
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        hideLoading();
    }

    @Override
    public void hideLoading() {

        super.hideLoading();
    }

    @Override
    public void refreshMembers() {

        if (mPresenter != null) {
            mPresenter.refresh();
        }
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        protected void onBind(MemberUserModel memberUserModel) {

            mPortrait.setup(Glide.with(GroupMemberActivity.this), memberUserModel.portrait);
            mName.setText(memberUserModel.name);
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {

            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }
    }
}
