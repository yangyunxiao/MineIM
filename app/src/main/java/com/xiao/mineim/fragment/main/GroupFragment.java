package com.xiao.mineim.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.presenter.group.GroupsContract;
import com.xiao.factory.presenter.group.GroupsPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.ChatActivity;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends ViewFragment<GroupsContract.Presenter>
        implements GroupsContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private Adapter mAdapter;


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_group;
    }


    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new Adapter());

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Group group) {
                ChatActivity.show(getContext(), group);
            }
        });
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initFirstData() {
        super.initFirstData();

        mPresenter.start();
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        mEmptyView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.txt_member)
        TextView mMember;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {

            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());

            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }
    }

    private class Adapter extends RecyclerAdapter<Group> {

        @Override
        public int getItemViewType(int position, Group group) {
            return R.layout.cell_group_list;
        }

        @Override
        protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
            return new GroupFragment.ViewHolder(root);
        }
    }
}
