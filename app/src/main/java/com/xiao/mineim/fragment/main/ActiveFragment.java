package com.xiao.mineim.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.widget.GalleryView;
import com.xiao.mineim.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends BaseFragment {

    @BindView(R.id.main_gallery_portrait)
    GalleryView mGalleryPortrait;

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.d("ACTIVE_FRAGMENT" ,"initData");
        mGalleryPortrait.setup(getLoaderManager(), new GalleryView.SelectedChangedListener() {
            @Override
            public void onSelectedChanged(int selectedImageCount) {

            }
        });
    }
}
