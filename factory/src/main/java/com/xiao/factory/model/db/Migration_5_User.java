package com.xiao.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 5, database = AppDatabase.class)
public class Migration_5_User extends AlterTableMigration<User> {


    public Migration_5_User(Class<User> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();

        addColumn(SQLiteType.INTEGER, "isFollow");
    }
}
