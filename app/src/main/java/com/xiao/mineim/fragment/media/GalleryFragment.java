package com.xiao.mineim.fragment.media;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.widget.GalleryView;
import com.xiao.mineim.R;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.Ui;

import butterknife.BindView;

/**
 * 图片选择的Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangedListener {

    private GalleryView mGallery;

    private OnSelectedListener mSelectedListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        mGallery = rootView.findViewById(R.id.gallery_view);

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new TransBottomSheetDialog(getContext());
    }

    @Override
    public void onSelectedChanged(int selectedImageCount) {

        if (selectedImageCount > 0) {

            dismiss();

            if (mSelectedListener != null) {

                String[] paths = mGallery.getSelectedImagePaths();

                mSelectedListener.onSelectedImage(paths[0]);

                mSelectedListener = null;
            }
        }
    }

    public GalleryFragment setSelectedListener(OnSelectedListener selectedListener) {
        this.mSelectedListener = selectedListener;
        return this;
    }

    public interface OnSelectedListener {

        void onSelectedImage(String imagePath);
    }


    //去除顶部statusBar黑色
    public static class TransBottomSheetDialog extends BottomSheetDialog {

        public TransBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        protected TransBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window dialogWindow = getWindow();

            if (dialogWindow == null)
                return;

            Resources resources = getContext().getResources();

            int screenHeight = resources.getDisplayMetrics().heightPixels;

//            int statusHeight = (int) Ui.dipToPx(resources, resources.getDimension(R.dimen.statusBarSize));
            int statusHeight = getStatusBarHeight(getContext());
            int dialogHeight = screenHeight - statusHeight;

            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight );
        }

        private int getStatusBarHeight(Context context) {
            int statusBarHeight = 0;
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
            return statusBarHeight;
        }
    }
}
