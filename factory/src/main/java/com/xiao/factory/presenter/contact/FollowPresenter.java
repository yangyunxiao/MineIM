package com.xiao.factory.presenter.contact;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BasePresenter;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class FollowPresenter extends BasePresenter<FollowContact.View>
        implements FollowContact.Presenter, DataSource.Callback<UserCard> {

    public FollowPresenter(FollowContact.View view) {
        super(view);
    }

    @Override
    public void follow(String userId) {
        start();
        UserHelper.follow(userId, this);
    }

    @Override
    public void onDataLoadSuccess(final UserCard userCard) {

        final FollowContact.View view = getView();

        if (view != null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }

    @Override
    public void onDataLoadFailed(final int failedMsg) {
        final FollowContact.View view = getView();
        if (view != null){

            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(failedMsg);
                }
            });
        }
    }
}
