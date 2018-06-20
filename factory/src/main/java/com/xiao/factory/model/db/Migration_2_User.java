package com.xiao.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

@Migration(version = 2, database = AppDatabase.class)
public class Migration_2_User extends AlterTableMigration<User> {


    public Migration_2_User(Class<User> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();

        addColumn(SQLiteType.INTEGER, "follow");
    }
}
