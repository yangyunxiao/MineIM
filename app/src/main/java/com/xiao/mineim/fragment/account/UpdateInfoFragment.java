package com.xiao.mineim.fragment.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.xiao.common.app.Application;
import com.xiao.common.app.BaseFragment;
import com.xiao.common.widget.PortraitView;
import com.xiao.mineim.R;
import com.xiao.mineim.fragment.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户信息的更新
 */
public class UpdateInfoFragment extends BaseFragment {


    @BindView(R.id.update_portrait)
    PortraitView mPortrait;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {

            final Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null) {

                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {

            final Throwable cropError = UCrop.getError(data);

        }

    }

    private void loadPortrait(Uri resultUri) {

        Glide.with(this)
                .load(resultUri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
    }
}
