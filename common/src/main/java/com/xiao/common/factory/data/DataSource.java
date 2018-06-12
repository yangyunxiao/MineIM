package com.xiao.common.factory.data;

import android.support.annotation.StringRes;

public interface DataSource {

    interface Callback<T> extends SuccessCallback<T>, FailedCallback {


    }

    interface SuccessCallback<T> {

        void onDataLoadSuccess(T t);

    }

    interface FailedCallback {

        //数据加载失败
        void onDataLoadFailed(@StringRes int failedMsg);
    }
}
