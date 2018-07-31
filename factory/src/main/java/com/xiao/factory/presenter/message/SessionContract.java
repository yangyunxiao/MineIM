package com.xiao.factory.presenter.message;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.Session;

/**
 * Created by xiao on 2018/7/31.
 *
 */

public interface SessionContract {

    //不需要什么，直接复用start即可
    interface Presenter extends BaseContract.Presenter{

    }


    interface View extends BaseContract.RecyclerView<Presenter,Session>{

    }
}
