package com.xiao.common.widget.recycler;

/**
 * Created by xiao on 2018/6/4.
 *
 */

public interface AdapterCallback<Data> {

    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
