package com.xiao.common.factory.presenter;

import android.support.v7.util.DiffUtil;

import com.xiao.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * 对Recycler进行再次的Presenter封装
 */

public class BaseRecyclerPresenter<ViewModel, View extends BaseContract.RecyclerView>
        extends BasePresenter<View> {
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 异步刷新一堆新数据到界面中
     */
    protected void refreshData(final List<ViewModel> dataList) {

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                View view = getView();
                if (view == null) {
                    return;
                }

                RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }


    /**
     * 刷新界面操作， 该操作可以保证执行方法在主线程运行
     *
     * @param diffResult 差异的结果集
     * @param dataList   具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                //主线程运行
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewModel> dataList) {

        View view = getView();
        if (view == null) {
            return;
        }

        //基本的更新数据并刷新界面
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();

        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);

        //通知界面刷新占位符
        view.onAdapterDataChanged();

        //进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }
}
