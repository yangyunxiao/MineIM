package com.xiao.common.factory.presenter;

import android.support.annotation.StringRes;

import com.xiao.common.widget.recycler.RecyclerAdapter;

public interface BaseContract {

    interface View<T extends Presenter> {

        //公共的显示一个错误字符串
        void showError(@StringRes int error);

        //公共的显示进度条
        void showLoading();

        //设置一个Presenter
        void setPresenter(T presenter);

    }

    interface Presenter {

        //共用的开始触发
        void start();

        //共用的销毁触发
        void destroy();

    }

    //基本的一个列表View的职责
    interface RecyclerView<T extends Presenter, ViewModel> extends View<T> {

        //界面端只能刷新整个数据结合  不能精确到每一条数据的更新
        //void onDone(List<User> users);

        //拿到一个适配器，主动进行刷新
        RecyclerAdapter<ViewModel> getRecyclerAdapter();

        //当时配置数据更改的时候触发操作
        void onAdapterDataChanged();
    }


}
