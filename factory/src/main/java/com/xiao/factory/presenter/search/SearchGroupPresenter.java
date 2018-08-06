package com.xiao.factory.presenter.search;

import android.support.annotation.StringRes;

import com.xiao.common.factory.data.DataSource;
import com.xiao.common.factory.presenter.BasePresenter;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>> {

    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        searchCall = GroupHelper.search(content, this);
    }

    @Override
    public void onDataLoadSuccess(final List<GroupCard> groupCards) {

        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {

                    view.onSearchDone(groupCards);
                }
            });
        }
    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {

        final SearchContract.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {

                    view.showError(failedMsg);
                }
            });
        }
    }
}
