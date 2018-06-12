package com.xiao.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiao.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {

    private final List<Data> mDataList;

    private AdapterListener mAdapterListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener mAdapterListener) {
        this(new ArrayList<Data>(), null);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;

        this.mAdapterListener = listener;

    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);

        ViewHolder<Data> viewHolder = onCreateViewHolder(root, viewType);

        root.setOnClickListener(this);

        root.setOnLongClickListener(this);

        root.setTag(R.id.recycler_view_holder, viewHolder);

        viewHolder.unbinder = ButterKnife.bind(viewHolder, root);

        viewHolder.mCallback = this;

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {

        return getItemViewType(position, mDataList.get(position));

    }

    @LayoutRes
    public abstract int getItemViewType(int position, Data data);

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void add(Data data) {

        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void add(Data... dataList) {

        if (dataList != null && dataList.length > 0) {

            int startPosition = mDataList.size();

            Collections.addAll(mDataList, dataList);

            notifyItemRangeInserted(startPosition, dataList.length);
        }
    }

    public void add(Collection<Data> dataList) {

        if (dataList != null && dataList.size() > 0) {

            int startPosition = mDataList.size();

            mDataList.addAll(dataList);

            notifyItemRangeInserted(startPosition, dataList.size());
        }
    }

    public void clear() {

        mDataList.clear();

        notifyDataSetChanged();
    }

    public void replace(Collection<Data> dataList) {

        mDataList.clear();

        if (dataList != null && dataList.size() > 0) {

            mDataList.addAll(dataList);
        }

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {

        Data data = mDataList.get(position);

        holder.bind(data);

    }

    @Override
    public void onClick(View v) {

        ViewHolder<Data> viewHolder = (ViewHolder<Data>) v.getTag(R.id.recycler_view_holder);

        if (this.mAdapterListener != null) {

            int position = viewHolder.getAdapterPosition();

            mAdapterListener.onItemClick(viewHolder, mDataList.get(position));
        }

    }

    @Override
    public boolean onLongClick(View v) {

        ViewHolder<Data> viewHolder = (ViewHolder<Data>) v.getTag(R.id.recycler_view_holder);

        if (this.mAdapterListener != null) {

            int position = viewHolder.getAdapterPosition();

            mAdapterListener.onItemLongClick(viewHolder, mDataList.get(position));

            return true;
        }

        return false;
    }

    public void setAdapterListener(AdapterListener adapterListener) {

        this.mAdapterListener = adapterListener;
    }

    public interface AdapterListener<Data> {

        void onItemClick(ViewHolder viewHolder, Data data);

        void onItemLongClick(ViewHolder viewHolder, Data data);
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {

        //得到当前ViewHolder的坐标
        int position = holder.getAdapterPosition();
        if (position > 0) {
            mDataList.remove(position);
            mDataList.add(position, data);
            notifyItemChanged(position);
        }
    }

    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder unbinder;

        private AdapterCallback<Data> mCallback;

        public ViewHolder(View itemView) {
            super(itemView);

        }

        void bind(Data data) {
            onBind(data);
        }

        protected abstract void onBind(Data data);

        public void updateData(Data data) {

            mCallback.update(data, this);
        }
    }
}
