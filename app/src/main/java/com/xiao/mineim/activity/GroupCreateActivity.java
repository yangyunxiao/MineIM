package com.xiao.mineim.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.Application;
import com.xiao.common.app.ViewToolbarActivity;
import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.common.widget.PortraitView;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.presenter.group.GroupCreateContract;
import com.xiao.factory.presenter.group.GroupCreatePresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupCreateActivity extends ViewToolbarActivity<GroupCreateContract.Presenter>
        implements GroupCreateContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.edit_name)
    EditText mName;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    private String mPortraitPath;

    private Adapter mAdapter;

    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupCreateActivity.class));
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public void onCreateSucceed() {

        hideLoading();
        Application.showToast(R.string.label_group_create_succeed);
        finish();
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

        hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            onCreateGroupClick();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建群操作
     */
    private void onCreateGroupClick() {

        hideSoftKeyboard();
        String name = mName.getText().toString();
        String desc = mDesc.getText().toString();
        mPresenter.create(name, desc, mPortraitPath);
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftKeyboard() {

        View view = getCurrentFocus();
        if (view == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {

        new GalleryFragment().setSelectedListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String imagePath) {

                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                options.setCompressionQuality(96);

                File tmpPath = Application.getPortraitTmpFile();

                UCrop.of(Uri.fromFile(new File(imagePath)), Uri.fromFile(tmpPath))
                        .withAspectRatio(1, 1) //1比1的比例
                        .withMaxResultSize(520, 520)//返回的最大尺寸
                        .withOptions(options)//相关参数
                        .start(GroupCreateActivity.this);

            }
        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }


    private void loadPortrait(Uri resultUri) {

        mPortraitPath = resultUri.getPath();

        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {

                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {

            Application.showToast(R.string.data_rsp_error_unknown);
        }

    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    private class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel> {

        @Override
        public int getItemViewType(int position, GroupCreateContract.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
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
        void onGroupMemberChecked(boolean isChecked) {

            mPresenter.changeSelect(mData, isChecked);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel viewModel) {

            mPortrait.setup(Glide.with(GroupCreateActivity.this), viewModel.author);
            mName.setText(viewModel.author.getName());
            mSelect.setChecked(viewModel.isSelected);
        }
    }
}
