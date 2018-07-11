package com.xiao.factory.presenter.contact;

import com.xiao.common.factory.presenter.BasePresenter;
import com.xiao.factory.Factory;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 个人信息页面Presenter层实现
 */

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {

    private User mUser;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public User getPersonalInfo() {
        return mUser;
    }

    @Override
    public void start() {

        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {

                PersonalContract.View view = getView();

                if (view != null) {
                    String userId = view.getUserId();
                    User user = UserHelper.searchUserFromNet(userId);
                    onLoaded(view, user);
                }
            }
        });
    }

    private void onLoaded(final PersonalContract.View view, final User user) {
        this.mUser = user;

        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());

        //是否已经关注
        final boolean isFollow = isSelf || user.isFollow();

        //是否允许聊天
        final boolean allowChat = isFollow && !isSelf;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {

                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowChat(allowChat);
            }
        });
    }

}
