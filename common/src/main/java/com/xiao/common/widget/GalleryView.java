package com.xiao.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiao.common.R;
import com.xiao.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GalleryView extends RecyclerView {

    private static final int LOADER_ID = 0x100;

    //最大选中图片爽
    private static final int MAX_SELECT_IMAGE_COUNT = 3;

    //最小选中的图片大小
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;

    private List<Image> mSelectedImages = new LinkedList<>();

    private Adapter mAdapter = new Adapter();

    private LoaderCallback mLoaderCallback = new LoaderCallback();

    private SelectedChangedListener mSelectedChangedListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    private void init() {

        setLayoutManager(new GridLayoutManager(getContext(), 4));

        setAdapter(mAdapter);

        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListener<Image>() {

            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Image image) {

                if (onItemSelected(image)) {

                    viewHolder.updateData(image);

                }

            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder viewHolder, Image image) {

            }
        });
    }


    /**
     * 初始化方法
     *
     * @return 返回LoaderId用于销毁Loader
     */
    public int setup(LoaderManager loaderManager, SelectedChangedListener selectedChangedListener) {
        mSelectedChangedListener = selectedChangedListener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }


    /**
     * 获取所有选中图片的路径数组
     */
    public String[] getSelectedImagePaths() {

        int selectedImageCount = mSelectedImages.size();

        String[] paths = new String[selectedImageCount];

        for (int index = 0; index < selectedImageCount; index++) {

            paths[index] = mSelectedImages.get(index).path;
        }

        return paths;

    }

    /**
     * 清除选中状态
     */
    public void clearSelectedImages() {

        for (Image image : mSelectedImages) {

            image.isSelected = false;

        }

        mSelectedImages.clear();

        mAdapter.notifyDataSetChanged();

    }


    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,    //图片数据库中存储的ID
                MediaStore.Images.Media.DATA,   //图片的路径信息
                MediaStore.Images.Media.DATE_ADDED // 图片的创建时间
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //创建一个Loader
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            //当Loader加载完毕
            List<Image> images = new ArrayList<>();

            if (data != null) {

                int count = data.getCount();

                if (count > 0) {

                    data.moveToFirst();

                    int indexID = data.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

                    int indexPath = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    int indexDate = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

                    do {
                        //循环读取知道没有下一行数据
                        int ID = data.getInt(indexID);
                        String imagePath = data.getString(indexPath);
                        long addDate = data.getLong(indexDate);

                        File imageFile = new File(imagePath);

                        if (!imageFile.exists() && imageFile.length() < MIN_IMAGE_FILE_SIZE) {
                            //如果没有图片或者图片尺寸大小不符合规定 则跳过
                            continue;
                        }

                        Image image = new Image();
                        image.path = imagePath;
                        image.isSelected = false;
                        image.date = addDate;
                        image.ID = ID;
                        images.add(image);
                    } while (data.moveToNext());

                }

            }
            updateSource(images);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            //当Loader销毁或者重置了
            updateSource(null);
        }
    }

    /**
     * 更新图片数据源  刷新视图
     */
    private void updateSource(List<Image> images) {

        mAdapter.replace(images);

    }

    /**
     * 点击选中这张图
     */
    private boolean onItemSelected(Image image) {

        boolean notifyRefresh;

        if (mSelectedImages.contains(image)) {

            mSelectedImages.remove(image);

            image.isSelected = false;

            notifyRefresh = true;

        } else {

            if (mSelectedImages.size() >= MAX_SELECT_IMAGE_COUNT) {

                String toastMaxSelectedTip = String.format(
                        getResources().getString(R.string.label_gallery_select_max_size),
                        MAX_SELECT_IMAGE_COUNT);
                //提示用户选中数量大于限定数
                Toast.makeText(getContext(), toastMaxSelectedTip, Toast.LENGTH_SHORT).show();

                notifyRefresh = false;

            } else {

                //添加图片  更改选中状态
                mSelectedImages.add(image);

                image.isSelected = true;

                notifyRefresh = true;
            }

        }


        if (notifyRefresh) {

            notifySelectedChanged();

        }
        return true;
    }

    /**
     * 通知监听者数据改变了
     */
    private void notifySelectedChanged() {

        if (mSelectedChangedListener != null) {
            mSelectedChangedListener.onSelectedChanged(mSelectedImages.size());
        }

    }

    public interface SelectedChangedListener {

        void onSelectedChanged(int selectedImageCount);

    }

    /**
     * 内部数据结构
     */
    private static class Image {

        //数据库中的ID
        int ID;

        //图片的路径
        String path;

        //图片的创建时间
        long date;

        //是否已被选中
        boolean isSelected;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        public int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private ImageView mPic;

        private View mShade;

        private CheckBox mCheck;

        public ViewHolder(View itemView) {
            super(itemView);

            mPic = (ImageView) itemView.findViewById(R.id.gallery_image_item);
            mCheck = (CheckBox) itemView.findViewById(R.id.gallery_check);
            mShade = itemView.findViewById(R.id.gallery_view_shade);

        }

        @Override
        protected void onBind(Image image) {

            Glide.with(getContext())
                    .load(image.path) //加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不使用缓存直接从原图加载
                    .centerCrop() //居中剪切
                    .placeholder(R.color.grey_200)
                    .into(mPic);

            mShade.setVisibility(image.isSelected ? View.VISIBLE : INVISIBLE);

            mCheck.setChecked(image.isSelected);
            Log.e("GALLERYVIEW","" + image.isSelected);
            mCheck.setVisibility(View.VISIBLE);

        }
    }
}
