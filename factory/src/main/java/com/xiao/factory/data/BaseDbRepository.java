package com.xiao.factory.data;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.data.DbDataSource;
import com.xiao.common.utils.CollectionUtil;
import com.xiao.factory.data.helper.DbHelper;
import com.xiao.factory.model.db.BaseDbModel;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * 基础的数据库仓库
 * 实现对数据库的基本的监听操作
 */

public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data>,
        QueryTransaction.QueryResultListCallback<Data> {

    private SuccessCallback<List<Data>> successCallback;
    private final List<Data> dataList = new LinkedList<>();
    private Class<Data> dataClass;

    public BaseDbRepository() {

        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {

        this.successCallback = callback;
        //进行数据库监听的操作
        registerDbChangedListener();

    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {

        //数据库加载数据成功
        if (tResult.size() == 0) {
            dataList.clear();
            notifyDataChange();
            return;
        }

        Data[] data = CollectionUtil.toArray(tResult, dataClass);

        onDataSave(data);

    }

    /**
     * 数据库统一通知的地方  增加/更改
     */
    @Override
    public void onDataSave(Data... list) {

        boolean isChanged = false;

        for (Data data : list) {
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }

        //有数据变更 ， 进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    @Override
    public void onDataDelete(Data... list) {

    }

    private void insertOrUpdate(Data data) {

        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    /**
     * 添加数据
     */
    private void insert(Data data) {
        dataList.add(data);
    }

    /**
     * 更新操作，更新某个坐标下的数据
     */
    private void replace(int index, Data data) {

        dataList.remove(index);
        dataList.add(index, data);
    }

    /**
     * 查询数据是否在当前缓存数据中，如果存在则返回坐标  不存在则返回-1
     */
    protected int indexOf(Data newData) {

        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void dispose() {

        this.successCallback = null;
        DbHelper.removeChangedListener(dataClass, this);
        dataList.clear();
    }

    /**
     * 数据过滤
     */
    protected abstract boolean isRequired(Data data);


    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener() {
        DbHelper.addChangedListener(dataClass, this);
    }

    /**
     * 通知界面刷新操作
     */
    private void notifyDataChange() {

        SuccessCallback<List<Data>> callback = this.successCallback;
        if (callback != null) {
            callback.onDataLoadSuccess(dataList);
        }
    }
}
