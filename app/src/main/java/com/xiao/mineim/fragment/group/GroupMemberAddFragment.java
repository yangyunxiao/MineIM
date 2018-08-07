package com.xiao.mineim.fragment.group;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.factory.data.DataSource;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.presenter.group.GroupCreateContract;
import com.xiao.factory.presenter.group.GroupMemberAddContract;
import com.xiao.factory.presenter.group.GroupMemberAddPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.media.GalleryFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMemberAddFragment extends BottomSheetDialogFragment
        implements GroupMemberAddContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Adapter mAdapter;
    private GroupMemberAddContract.Presenter mPresenter;
    private Callback mCallback;

    public GroupMemberAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initPresenter();
        View root = inflater.inflate(R.layout.fragment_group_member_add, container, false);

        ButterKnife.bind(this, root);
        initRecycler();
        initToolbar();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    private void initToolbar() {

        mToolbar.inflateMenu(R.menu.group_create);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_create) {
                    if (mPresenter != null) {
                        mPresenter.submit();
                    }
                    return true;
                }
                return false;
            }
        });

        MenuItem item = mToolbar.getMenu().findItem(R.id.action_create);
        Drawable drawable = item.getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, UiCompat.getColor(getResources(), R.color.textPrimary));
        item.setIcon(drawable);
    }

    private void initRecycler() {

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    private void initPresenter() {

        new GroupMemberAddPresenter(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransBottomSheetDialog(getContext());
    }

    @Override
    public void showError(@StringRes int error) {

        if (mCallback != null) {
            mCallback.showError(error);
        }
    }

    @Override
    public void showLoading() {

        if (mCallback != null) {
            mCallback.showLoading();
        }
    }

    @Override
    public void setPresenter(GroupMemberAddContract.Presenter presenter) {

        mPresenter = presenter;
    }

    @Override
    public void onAddedSucceed() {

        if (mCallback != null) {
            mCallback.hideLoading();
            mCallback.refreshMembers();
        }
        dismiss();
    }

    @Override
    public String getGroupId() {
        return mCallback.getGroupId();
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        if (mCallback != null) {
            mCallback.hideLoading();
        }

    }

    private class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel> {

        @Override
        public int getItemViewType(int position, GroupCreateContract.ViewModel model) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupMemberAddFragment.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel> {

        @BindView(R.id.im_portrait)
        PortraitView mPortrait;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.cb_select)
        CheckBox mSelect;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked) {
            mPresenter.changeSelect(mData, checked);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel model) {
            mPortrait.setup(Glide.with(GroupMemberAddFragment.this), model.author);
            mName.setText(model.author.getName());
            mSelect.setChecked(model.isSelected);
        }
    }

    /**
     * Fragment 与 Activity之间交互的接口
     */
    public interface Callback {

        String getGroupId();

        void hideLoading();

        void showError(@StringRes int str);

        void showLoading();

        void refreshMembers();
    }
}
