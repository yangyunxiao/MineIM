package com.xiao.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.xiao.factory.data.group.GroupDataSource;
import com.xiao.factory.data.group.GroupsRepository;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.presenter.BaseSourcePresenter;
import com.xiao.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群列表Presenter层
 * Created by xiao on 2018/8/3.
 */

public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupDataSource, GroupsContract.View>
        implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoadSuccess(List<Group> groups) {

        final GroupsContract.View view = getView();
        if (view != null) {
            List<Group> old = view.getRecyclerAdapter().getItems();
            DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

            refreshData(result, groups);
        }
    }
}
