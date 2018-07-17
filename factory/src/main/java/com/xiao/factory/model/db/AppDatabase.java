package com.xiao.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库
 */
@Database(name = AppDatabase.DB_NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String DB_NAME = "AppDataBase";

    public static final int VERSION = 5;

}
