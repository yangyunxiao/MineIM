package com.xiao.mineim.fragment.search;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.presenter.search.SearchContract;
import com.xiao.factory.presenter.search.SearchGroupPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.PersonalActivity;
import com.xiao.mineim.activity.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群搜索
 */
public class SearchGroupFragment extends ViewFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.GroupView {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<GroupCard> mAdapter;

    public SearchGroupFragment() {

    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            public int getItemViewType(int position, GroupCard o) {
                return R.layout.cell_search_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });

        mEmptyView.bind(mRecyclerView);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);

    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        mAdapter.replace(groupCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);

    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.im_join)
        ImageView mJoin;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            mJoin.setEnabled(groupCard.getJoinAt() == null);
        }

        @OnClick(R.id.im_join)
        void onJoinClick() {

            PersonalActivity.show(getContext(), mData.getOwnerId());
        }

    }
}
