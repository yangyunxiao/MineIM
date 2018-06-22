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
import com.xiao.factory.presenter.contact.FollowContact;
import com.xiao.factory.presenter.contact.FollowPresenter;
import com.xiao.factory.presenter.search.SearchContactPresenter;
import com.xiao.factory.presenter.search.SearchContract;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
            return R.layout.cell_search_list;
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
    protected void initData() {
        super.initData();
        search("");
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


    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContact.View {

        @BindView(R.id.search_portrait)
        PortraitView portraitView;

        @BindView(R.id.search_text_name)
        TextView userName;

        @BindView(R.id.search_image_follow)
        ImageView follow;

        private FollowContact.Presenter mFollowPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            new FollowPresenter(this);

        }

        @Override
        protected void onBind(UserCard userCard) {
            portraitView.setup(Glide.with(SearchContactFragment.this), R.mipmap.default_portrait, userCard.getPortrait());
            userName.setText(userCard.getName());
            follow.setEnabled(!userCard.isFollow());

        }

        @OnClick(R.id.search_portrait)
        void onPortraitClick() {

        }

        @OnClick(R.id.search_image_follow)
        void onFollowClick() {

            mFollowPresenter.follow(mData.getId());

        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            // 更改当前界面状态
            if (follow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) follow.getDrawable()).stop();
                // 设置为默认的
                follow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }

        @Override
        public void showError(int error) {
            // 更改当前界面状态
            if (follow.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) follow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 设置进去
            follow.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContact.Presenter presenter) {
            mFollowPresenter = presenter;
        }
    }

}
