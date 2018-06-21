package com.xiao.factory.presenter.search;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.card.UserCard;

import java.util.List;

public interface SearchContract {


    interface Presenter extends BaseContract.Presenter{

        void search(String content);
    }

    interface ContactView extends BaseContract.View<Presenter>{

        void onSearchDone(List<UserCard> userCards);

    }

    interface GroupView extends BaseContract.View<Presenter>{

        void onSearchDone(List<GroupCard> groupCards);

    }
}
