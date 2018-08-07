package com.xiao.factory.presenter.group;

import android.support.annotation.StringRes;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BaseRecyclerPresenter;
import com.xiao.factory.Factory;
import com.xiao.factory.R;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.api.group.GroupMemberAddModel;
import com.xiao.factory.model.card.GroupMemberCard;
import com.xiao.factory.model.db.view.MemberUserModel;
import com.xiao.factory.model.db.view.UserSampleModel;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 群成员添加的逻辑
 * Created by xiao on 2018/8/7.
 */

public class GroupMemberAddPresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel,
        GroupMemberAddContract.View> implements GroupMemberAddContract.Presenter,
        DataSource.Callback<List<GroupMemberCard>> {
    private Set<String> users = new HashSet<>();

    public GroupMemberAddPresenter(GroupMemberAddContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();

        Factory.runOnAsync(loader);
    }

    @Override
    public void submit() {

        GroupMemberAddContract.View view = getView();
        view.showLoading();

        if (users.size() == 0) {
            view.showError(R.string.label_group_member_add_invalid);
            return;
        }

        GroupMemberAddModel model = new GroupMemberAddModel(users);
        GroupHelper.addMembers(view.getGroupId(), model, this);
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }
    }

    @Override
    public void onDataLoadSuccess(List<GroupMemberCard> groupMemberCards) {

        GroupMemberAddContract.View view = getView();

        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                GroupMemberAddContract.View view = getView();
                if (view == null) {
                    return;
                }

                view.onAddedSucceed();
            }
        });

    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {

        GroupMemberAddContract.View view = getView();

        if (view == null) {
            return;
        }

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                GroupMemberAddContract.View view = getView();
                if (view == null) {
                    return;
                }

                view.showError(failedMsg);
            }
        });
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {

            GroupMemberAddContract.View view = getView();

            if (view == null) {
                return;
            }

            List<UserSampleModel> contacts = UserHelper.getSampleContact();

            List<MemberUserModel> members = GroupHelper.getMemberUsers(view.getGroupId(), -1);

            for (MemberUserModel member : members) {

                int index = isContainInTheGroup(contacts, member.userId);

                if (index >= 0) {

                    contacts.remove(index);
                }

            }

            List<GroupCreateContract.ViewModel> models = new ArrayList<>();

            for (UserSampleModel contact : contacts) {

                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = contact;
                models.add(viewModel);
            }

            refreshData(models);

        }
    };

    private int isContainInTheGroup(List<UserSampleModel> contact, String groupUserId) {

        int index = 0;
        for (UserSampleModel model : contact) {
            if (model.getId().equalsIgnoreCase(groupUserId)) {

                return index;
            }

            index++;
        }

        return -1;
    }
}
