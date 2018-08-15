package com.xiao.mineim.fragment.main;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.face.Face;
import com.xiao.common.utils.DateTimeUtil;
import com.xiao.common.widget.EmptyView;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.Session;
import com.xiao.factory.presenter.message.SessionContract;
import com.xiao.factory.presenter.message.SessionPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.ChatActivity;

import net.qiujuer.genius.ui.Ui;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends ViewFragment<SessionContract.Presenter>
        implements SessionContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<Session> mAdapter;

    public ActiveFragment() {
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            public int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Session session) {
                ChatActivity.show(getContext(), session);
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
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {

        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());

            Spannable spannable = new SpannableString(session.getContent());
            //解析表情
            Face.decode(mContent, spannable, (int) Ui.dipToPx(getResources(), 20));
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : spannable);
            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}
