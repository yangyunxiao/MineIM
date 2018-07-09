package com.xiao.factory.presenter.contact;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.User;

/**
 * 联系人
 */

public interface ContactContract {

    interface Presenter extends BaseContract.Presenter {

    }


    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }

}
