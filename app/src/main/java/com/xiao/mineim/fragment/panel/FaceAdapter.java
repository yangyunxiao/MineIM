package com.xiao.mineim.fragment.panel;

import android.view.View;

import com.xiao.common.face.Face;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.mineim.R;

import java.util.List;


public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> been, AdapterListener<Face.Bean> listener) {
        super(been, listener);
    }

    @Override
    public int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}
