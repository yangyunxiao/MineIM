package com.xiao.mineim.fragment.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.presenter.contact.ContactContract;
import com.xiao.factory.presenter.contact.ContactPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.ChatActivity;
import com.xiao.mineim.activity.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends ViewFragment<ContactContract.Presenter>
        implements ContactContract.View {

    @BindView(R.id.contact_empty)
    EmptyView mEmpty;

    @BindView(R.id.contact_recycler)
    RecyclerView mContactRecycler;

    private RecyclerAdapter<User> mUserAdapter;

    @Override
    protected int getContentLayoutID() {

        return R.layout.fragment_contact;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        mContactRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactRecycler.setAdapter(mUserAdapter = new RecyclerAdapter<User>() {
            @Override
            public int getItemViewType(int position, User user) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });

        mUserAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, User user) {

                ChatActivity.show(getContext(), user);
            }
        });

        mEmpty.bind(mContactRecycler);
        setPlaceHolderView(mEmpty);
    }


    @Override
    protected void initFirstData() {
        super.initFirstData();

        //进行一次初次数据加载
        mPresenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {

        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return mUserAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        mPlaceHolderView.triggerOkOrEmpty(mUserAdapter.getItemCount() > 0);

    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(User user) {
            mPortraitView.setup(Glide.with(ContactFragment.this), R.mipmap.default_portrait, user.getPortrait());
            mName.setText(user.getName());
            mDesc.setText(user.getDesc());

        }


        @OnClick(R.id.im_portrait)
        void onPortraitClick() {

            PersonalActivity.show(getContext(), mData.getId());
        }

    }
}
