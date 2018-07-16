package com.xiao.common.factory.data;

import android.support.annotation.StringRes;

public interface DataSource {

    /**
     * 同时包含了成功和失败的回调接口
     *
     * @param <T> 任意类型
     */
    interface Callback<T> extends SuccessCallback<T>, FailedCallback {


    }

    /**
     * 细化操作成功
     *
     * @param <T>
     */
    interface SuccessCallback<T> {

        void onDataLoadSuccess(T t);

    }

    /**
     * 细化失败操作
     */
    interface FailedCallback {
        void onDataLoadFailed(@StringRes int failedMsg);
    }


    /**
     * 销毁操作
     */
    void dispose();
}
