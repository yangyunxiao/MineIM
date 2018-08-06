package com.xiao.mineim.fragment.message;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.adapter.TextWatcherAdapter;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.presenter.message.ChatContract;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.ChatActivity;
import com.xiao.mineim.fragment.panel.PanelFragment;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天fragment基础封装类
 */

public abstract class ChatFragment<InitModel>
        extends ViewFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel> {

    protected String mReceiverId;
    protected Adapter mAdapter;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.chat_edit_content)
    EditText mContent;

    @BindView(R.id.chat_image_submit)
    View mSubmit;

    //控制顶部面案与软键盘过度的Boss控件
    private AirPanel.Boss mPanelBoss;
    private PanelFragment mPanelFragment;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(ChatActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_chat_common;
    }

    @LayoutRes
    protected abstract int getHeaderLayoutId();

    @Override
    protected void initWidget(View rootView) {
        //拿到占位布局
        //替换顶部布局一定需要发生在super之前
        //防止控件绑定异常
        ViewStub stub = rootView.findViewById(R.id.view_stub_header);
        stub.setLayoutResource(getHeaderLayoutId());
        stub.inflate();
        super.initWidget(rootView);

        mPanelBoss = rootView.findViewById(R.id.lay_content);
        mPanelBoss.setup(new AirPanel.PanelListener() {
            @Override
            public void requestHideSoftKeyboard() {
                //请求隐藏软件盘
                Util.hideKeyboard(mContent);
            }
        });

        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);

        initToolBar();
        initAppbar();
        initEditContent();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    protected void initToolBar() {

        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    /**
     * 给Appbar设置一个监听，得到关闭和打开的进度
     */
    protected void initAppbar() {

        mAppBarLayout.addOnOffsetChangedListener(this);
    }


    private void initEditContent() {

        mContent.addTextChangedListener(new TextWatcherAdapter() {

            @Override
            public void afterTextChanged(Editable s) {

                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);

                // 设置状态 改变对应的Icon
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.chat_image_face)
    void onFaceClick() {

        mPanelBoss.openPanel();
        mPanelFragment.showFace();

    }

    @OnClick(R.id.chat_image_record)
    void onRecordClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showRecord();
    }

    @OnClick(R.id.chat_image_submit)
    void onSubmitClick() {

        if (mSubmit.isActivated()) {
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    private void onMoreClick() {
        //TODO
    }


    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //界面没有占位布局 ，Recycler 是一直显示着的 所以不需要做任何事情
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    /**
     * 内容的适配器
     */
    private class Adapter extends RecyclerAdapter<Message> {
        @Override
        public int getItemViewType(int position, Message message) {

            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());

            switch (message.getType()) {
                case Message.TYPE_STR:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {

            switch (viewType) {
                case R.layout.cell_chat_text_left:
                case R.layout.cell_chat_text_right:
                    return new TextHolder(root);
                case R.layout.cell_chat_pic_left:
                case R.layout.cell_chat_pic_right:
                    return new PicHolder(root);
                case R.layout.cell_chat_audio_left:
                case R.layout.cell_chat_audio_right:
                    return new AudioHolder(root);
                default:
                    return new TextHolder(root);
            }
        }
    }


    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {

            User sender = message.getSender();
            //进行数据加载  懒加载
            sender.load();

            mPortrait.setup(Glide.with(ChatFragment.this), sender);

            if (mLoading != null) {

                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {

                    //正在发送的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {
                    //发送失败状态，允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }

                //当状态是错误的才允许点击重新发送消息
                mPortrait.setEnabled(status == Message.STATUS_FAILED);

            }
        }

        /**
         * 重新发送消息
         */
        @OnClick(R.id.im_portrait)
        void onRePushClick() {

            if (mLoading != null && mPresenter.rePush(mData)) {

                //必须是自己发送的消息才有需要重新发送消息  状态改变之后需要重新刷新当前的信息
                updateData(mData);
            }
        }
    }

    /**
     * 文字的Holder
     */
    class TextHolder extends BaseHolder {

        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            mContent.setText(message.getContent());
        }
    }

    /**
     * 语音的Holder
     */
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }

    /**
     * 图片信息的Holder
     */
    class PicHolder extends BaseHolder {

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }
}
