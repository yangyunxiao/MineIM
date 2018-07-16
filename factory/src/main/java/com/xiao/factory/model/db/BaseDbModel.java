package com.xiao.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.xiao.factory.utils.DiffUiDataCallback;

/**
 * 基础的数据库Model
 */

public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
