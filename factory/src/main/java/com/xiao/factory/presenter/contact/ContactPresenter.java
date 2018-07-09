package com.xiao.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BasePresenter;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.model.db.AppDatabase;
import com.xiao.factory.model.db.User;
import com.xiao.factory.model.db.User_Table;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.utils.DiffUiDataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人界面Presenter层.
 */

public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        //加载数据库中的数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.follow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.phone, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {

                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();


        //加载网络数据
        UserHelper.refreshContracts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataLoadFailed(@StringRes int failedMsg) {
                //网络失败的情况下  因为本地本身有数据  所以不用管
            }

            @Override
            public void onDataLoadSuccess(List<UserCard> userCards) {

                final List<User> users = new ArrayList<User>();

                for (UserCard userCard :
                        userCards) {
                    users.add(userCard.build());

                }
                //使用事务保存到数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {

                        FlowManager.getModelAdapter(User.class).saveAll(users);
                    }
                }).build().execute();

                List<User> oldUsers = getView().getRecyclerAdapter().getItems();

                //使用此方法刷新数据会导致数据全部为新的集合 所以要使用新旧集合比对  选取两个集合的并集作为数据源
                //getView().getRecyclerAdapter().replace(users);
                diff(oldUsers, users);
            }
        });
    }

    private void diff(List<User> oldList, List<User> newList) {

        //进行数据比对
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //对比完成之后赋值操作
        getView().getRecyclerAdapter().replace(newList);

        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();


    }

}
