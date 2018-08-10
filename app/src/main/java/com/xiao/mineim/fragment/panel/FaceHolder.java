package com.xiao.mineim.fragment.panel;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.xiao.common.face.Face;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.mineim.R;

import butterknife.BindView;

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {

    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {

        if (bean != null
                && ((bean.preview instanceof Integer)
                || (bean.preview instanceof String))) {

            Glide.with(itemView.getContext())
                    .load(bean.preview)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.default_face)
                    .into(mFace);

        }
    }
}
