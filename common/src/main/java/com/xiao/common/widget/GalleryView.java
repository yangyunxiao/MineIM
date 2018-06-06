package com.xiao.common.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xiao.common.R;
import com.xiao.common.widget.recycler.RecyclerAdapter;

public class GalleryView extends RecyclerView {
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

    }

    private static class Image{


    }

    private class Adapter extends RecyclerAdapter<Image>{

        @Override
        public int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Image image) {

        }
    }
}
