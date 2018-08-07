package com.xiao.factory.presenter.group;

import com.xiao.common.factory.presenter.BaseRecyclerPresenter;
import com.xiao.factory.Factory;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.model.db.view.MemberUserModel;

import java.util.List;

public class GroupMemberPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMemberContract.View>
        implements GroupMemberContract.Presenter {
    public GroupMemberPresenter(GroupMemberContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {

        start();

        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMemberContract.View view = getView();

            if (view == null) {
                return;
            }

            String groupId = view.getGroupId();

            //传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);

        }
    };
}
