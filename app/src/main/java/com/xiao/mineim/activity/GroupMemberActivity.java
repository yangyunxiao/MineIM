package com.xiao.mineim.activity;

import android.os.Bundle;

import com.xiao.common.app.ViewToolbarActivity;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.presenter.group.GroupMemberContract;
import com.xiao.mineim.R;

public class GroupMemberActivity extends ViewToolbarActivity<GroupMemberContract.Presenter>
        implements GroupMemberContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
    }

    @Override
    protected int getContentLayoutID() {
        return 0;
    }

    @Override
    public String getGroupId() {
        return null;
    }

    @Override
    protected GroupMemberContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return null;
    }

    @Override
    public void onAdapterDataChanged() {

    }
}
