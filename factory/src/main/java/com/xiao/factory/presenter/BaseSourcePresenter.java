package com.xiao.factory.presenter;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.data.DbDataSource;
import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的Presenter定义
 */

public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SuccessCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null) {
            mSource.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
