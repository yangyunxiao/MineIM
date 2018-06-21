package com.xiao.mineim.fragment.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.presenter.search.SearchContactPresenter;
import com.xiao.factory.presenter.search.SearchContract;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.SearchActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 人搜索
 */
public class SearchContactFragment extends ViewFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.ContactView {

    @BindView(R.id.search_recycler_contact)
    RecyclerView mContactRecycler;

    @BindView(R.id.search_empty)
    EmptyView mEmptyView;

    private RecyclerAdapter<UserCard> mAdapter = new RecyclerAdapter<UserCard>() {

        @Override
        public int getItemViewType(int position, UserCard userCard) {
            return R.layout.cell_contact_list;
        }

        @Override
        protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
            return new SearchContactFragment.ViewHolder(root);
        }
    };

    public SearchContactFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mContactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mContactRecycler.setAdapter(mAdapter);

        mEmptyView.bind(mContactRecycler);

    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_search_contact;
    }


    @Override
    public void onSearchDone(List<UserCard> userCards) {

        mAdapter.replace(userCards);
        mEmptyView.triggerOkOrEmpty(userCards.size() > 0);
    }

    @Override
    public void search(String content) {

        mPresenter.search(content);
    }


    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchContactPresenter(this);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {

        @BindView(R.id.contact_portrait_user)
        PortraitView portraitView;

        @BindView(R.id.contact_text_name)
        TextView userName;

        @BindView(R.id.contact_image_follow)
        ImageView follow;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(UserCard userCard) {

            portraitView.setup(Glide.with(SearchContactFragment.this), userCard.getPortrait());
            userName.setText(userCard.getName());
            follow.setEnabled(!userCard.isFollow());

        }
    }

}
