package com.xiao.mineim.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.User;
import com.xiao.mineim.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {

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
                return 0;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return null;
            }
        });
    }
}
