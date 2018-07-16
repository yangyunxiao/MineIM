package com.xiao.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.xiao.factory.data.BaseDbRepository;
import com.xiao.factory.model.db.AppDatabase;
import com.xiao.factory.model.db.BaseDbModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 数据库的辅助工具类
 * 辅助完成： 增删改
 */

public class DbHelper {

    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {

    }

    /**
     * 观察者的集合
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听者中  ， 获取某一个表的所有监听者
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {

        if (changedListeners.containsKey(modelClass)) {

            return changedListeners.get(modelClass);
        }

        return null;

    }


    /**
     * 新增或者修改的统一方法
     *
     * @param tClass  传递一个Class信息
     * @param models  这个Class对应的实例的数组
     * @param <Model> 这个市里的泛型  限定条件是BaseModel
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {

        //拦截非法数据源
        if (models == null || models.length == 0) {
            return;
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);

        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {

                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);

                adapter.saveAll(Arrays.asList(models));

                instance.notifySave(tClass, models);
            }
        }).build().execute();

    }

    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass,
                                                            final Model... models) {

        //查找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {

            for (ChangedListener<Model> listener : listeners) {

                listener.onDataSave(models);
            }

        }

        // TODO 处理例外的情况
    }

    public static <Model extends BaseDbModel<Model>> void addChangedListener(Class<Model> tClass,
                                                                             ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);

        if (changedListeners == null){

            //初始化容器
            changedListeners = new HashSet<>();
            instance.changedListeners.put(tClass,changedListeners);
        }
        changedListeners.add(listener);
    }

    /**
     * 通知监听器
     */
    public interface ChangedListener<Data extends BaseModel> {

        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }
}
