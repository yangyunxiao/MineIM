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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.Application;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.face.Face;
import com.xiao.common.tools.AudioPlayHelper;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.adapter.TextWatcherAdapter;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.db.Message;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.presenter.message.ChatContract;
import com.xiao.factory.utils.FileCache;
import com.xiao.mineim.App;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.ChatActivity;
import com.xiao.mineim.fragment.panel.PanelFragment;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天fragment基础封装类
 */

public abstract class ChatFragment<InitModel>
        extends ViewFragment<ChatContract.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContract.View<InitModel>, PanelFragment.PanelCallback {

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

    private AudioPlayHelper<AudioHolder> mAudioPlayer;

    private FileCache<AudioHolder> mAudioFileCache;

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
        mPanelFragment.setup(this);

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

    @Override
    public void onStart() {
        super.onStart();
        mAudioPlayer = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<AudioHolder>() {
            @Override
            public void onPlayStart(AudioHolder audioHolder) {
                audioHolder.onPlayStart();
            }

            @Override
            public void onPlayStop(AudioHolder audioHolder) {
                audioHolder.onPlayStop();
            }

            @Override
            public void onPlayError(AudioHolder audioHolder) {
                App.showToast(R.string.toast_audio_play_error);
            }
        });

        mAudioFileCache = new FileCache<>("audio/cache", "mp3", new FileCache.CacheListener<AudioHolder>() {
            @Override
            public void onDownloadSucceed(final AudioHolder audioHolder, final File file) {
                Run.onUiAsync(new Action() {
                    @Override
                    public void call() {
                        mAudioPlayer.trigger(audioHolder, file.getAbsolutePath());
                    }
                });
            }

            @Override
            public void onDownloadFailed(AudioHolder audioHolder) {

                Application.showToast(R.string.toast_download_error);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioPlayer.destroy();
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
        mPanelBoss.openPanel();
        mPanelFragment.showGallery();
    }

    @Override
    public void onSendGallery(String[] paths) {
        mPresenter.pushImages(paths);
    }

    @Override
    public void onRecordDone(File file, long time) {

        mPresenter.pushAudio(file.getAbsolutePath(), time);
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

    @Override
    public EditText getInputEditText() {
        return mContent;
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

            Spannable spannable = new SpannableString(message.getContent());
            //解析表情
            Face.decode(mContent, spannable, (int) Ui.dipToPx(getResources(), 20));

            mContent.setText(spannable);
        }
    }

    /**
     * 语音的Holder
     */
    class AudioHolder extends BaseHolder {

        @BindView(R.id.txt_content)
        TextView mContent;

        @BindView(R.id.im_audio_track)
        ImageView mAudioTrack;

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            String attach = TextUtils.isEmpty(message.getAttach()) ? "0" : message.getAttach();

            mContent.setText(formatTime(attach));
        }

        void onPlayStart() {
            mAudioTrack.setVisibility(View.VISIBLE);
        }

        void onPlayStop() {
            mAudioTrack.setVisibility(View.INVISIBLE);
        }


        private String formatTime(String attach) {

            float time;

            try {

                time = Float.parseFloat(attach) / 1000f;

            } catch (Exception exception) {
                Log.e("ERROR", exception.toString());
                time = 0;
            }

            String shortTime = String.valueOf(Math.round(time * 10f) / 10f);
            shortTime = shortTime.replaceAll("[.]0+?$|0+?$", "");
            return String.format("%s″", shortTime);
        }
    }

    /**
     * 图片信息的Holder
     */
    class PicHolder extends BaseHolder {

        @BindView(R.id.im_image)
        ImageView mContent;

        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);

            String content = message.getContent();

            Glide.with(ChatFragment.this)
                    .load(content)
                    .fitCenter()
                    .into(mContent);
        }
    }
}
