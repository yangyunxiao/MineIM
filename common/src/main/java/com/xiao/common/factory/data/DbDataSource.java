package com.xiao.common.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 */

public interface DbDataSource<Data> extends DataSource {

    /**
     * 有一个基本的数据源加载方法
     */
    void load(SuccessCallback<List<Data>> callback);
}
