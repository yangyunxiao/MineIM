package com.xiao.factory.presenter.search;

import android.support.annotation.StringRes;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BasePresenter;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

public class SearchContactPresenter extends BasePresenter<SearchContract.ContactView>
        implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {
    private Call mSearchCall;


    public SearchContactPresenter(SearchContract.ContactView view) {
        super(view);
    }

    @Override
    public void search(String content) {

        start();

        if (mSearchCall != null && !mSearchCall.isCanceled()) {

            mSearchCall.cancel();
        }

        mSearchCall = UserHelper.searchUser(content, this);
    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {

        final SearchContract.ContactView view = getView();

        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {

                    view.showError(failedMsg);
                }
            });
        }
    }

    @Override
    public void onDataLoadSuccess(final List<UserCard> userCards) {

        final SearchContract.ContactView view = getView();

        if (view != null) {

            Run.onUiAsync(new Action() {
                @Override
                public void call() {

                    view.onSearchDone(userCards);
                }
            });
        }
    }
}
