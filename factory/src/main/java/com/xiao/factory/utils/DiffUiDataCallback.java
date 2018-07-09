package com.xiao.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * 对比
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    /**
     * 比较两个对象是否为对应同一实体，比如ID相等的User
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldData = mOldList.get(oldItemPosition);
        T newData = mNewList.get(newItemPosition);

        return newData.isSame(oldData);
    }

    /**
     * 判断对象为同实体之后  进一步判断是否有数据更改
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldData = mOldList.get(oldItemPosition);
        T newData = mNewList.get(newItemPosition);

        return newData.isUiContentSame(oldData);
    }

    //进行比较的数据类型
    public interface UiDataDiffer<T> {

        //对比新旧数据是否为同一个数据
        boolean isSame(T old);

        //对比新旧数据内容是否相同
        boolean isUiContentSame(T old);

    }
}
