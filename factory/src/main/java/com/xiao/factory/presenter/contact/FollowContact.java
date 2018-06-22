package com.xiao.factory.presenter.contact;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.card.UserCard;

public interface FollowContact {

    interface Presenter extends BaseContract.Presenter{

        void follow(String userId);
    }

    interface View extends BaseContract.View<Presenter>{
        
        void onFollowSucceed(UserCard userCard);
    }
}
