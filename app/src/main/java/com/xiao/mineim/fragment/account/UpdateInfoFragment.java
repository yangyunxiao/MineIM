package com.xiao.mineim.fragment.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiao.common.app.Application;
import com.xiao.common.app.ViewFragment;
import com.xiao.common.widget.PortraitView;
import com.xiao.factory.presenter.user.UpdateInfoContract;
import com.xiao.factory.presenter.user.UpdateInfoPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.MainActivity;
import com.xiao.mineim.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户信息的更新
 */
public class UpdateInfoFragment extends ViewFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {

    private static final String TAG = UpdateInfoFragment.class.getName();

    @BindView(R.id.update_image_sex)
    ImageView mSex;

    @BindView(R.id.update_edit_desc)
    EditText mDesc;

    @BindView(R.id.update_portrait)
    PortraitView mPortrait;

    @BindView(R.id.update_button_submit)
    Button mSubmit;

    @BindView(R.id.loading)
    Loading mLoading;

    /**
     * 头像路径
     */
    private String mPortraitPath;

    private boolean isMan = true;

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.update_portrait)
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
                        .start(getActivity());

            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }


    private void loadPortrait(Uri resultUri) {

        mPortraitPath = resultUri.getPath();

        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);

    }

    @OnClick(R.id.update_image_sex)
    void onSexClick() {

        isMan = !isMan;

        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);

        mSex.setImageDrawable(drawable);

        mSex.getBackground().setLevel(isMan ? 0 : 1);
    }

    @OnClick(R.id.update_button_submit)
    void onSubmitClick() {

        String desc = mDesc.getText().toString();

        mPresenter.update(mPortraitPath, desc, isMan);

    }

    @Override
    public void showError(@StringRes int error) {
        super.showError(error);

        mLoading.stop();
        mDesc.setEnabled(true);
        mSex.setEnabled(true);
        mPortrait.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        mDesc.setEnabled(false);
        mSex.setEnabled(false);
        mPortrait.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void updateSucceed() {

        MainActivity.show(getActivity());
        getActivity().finish();
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {

        return new UpdateInfoPresenter(this);

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

}
