package com.xiao.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

public class DbFlowExclusionStrategy implements ExclusionStrategy {
    //跳过的字段
    @Override
    public boolean shouldSkipField(FieldAttributes f) {


        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    //跳过的Class
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
