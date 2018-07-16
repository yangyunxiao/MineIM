package com.xiao.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.widget.recycler.RecyclerAdapter;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.data.user.ContactDataSource;
import com.xiao.factory.data.user.ContactRepository;
import com.xiao.factory.model.db.User;
import com.xiao.factory.presenter.BaseSourcePresenter;
import com.xiao.factory.utils.DiffUiDataCallback;
import java.util.List;

/**
 * 联系人界面Presenter层.
 */

public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SuccessCallback<List<User>> {


    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        UserHelper.refreshContracts();

    }


    @Override
    public void onDataLoadSuccess(List<User> users) {

        final ContactContract.View view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> oldList = adapter.getItems();
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshData(result, users);

    }
}
