package com.xiao.factory.presenter.message;

import android.support.v7.util.DiffUtil;
import com.xiao.factory.data.message.SessionDataSource;
import com.xiao.factory.data.message.SessionRepository;
import com.xiao.factory.model.db.Session;
import com.xiao.factory.presenter.BaseSourcePresenter;
import com.xiao.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的Presenter
 * Created by xiao on 2018/7/31.
 */

public class SessionPresenter extends
        BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoadSuccess(List<Session> sessions) {

        SessionContract.View view = getView();
        if (view == null)
            return;

        List<Session> oldSessions = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(oldSessions, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        refreshData(result, sessions);
    }
}
