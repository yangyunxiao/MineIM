package com.xiao.factory.presenter.group;

import com.xiao.common.factory.presenter.BaseContract;
import com.xiao.factory.model.db.Group;

/**
 * 群列表界面契约
 * Created by xiao on 2018/8/3.
 */

public interface GroupsContract {

    /**
     * 不需要任何额外的定义，开始只需调用start即可
     */
    interface Presenter extends BaseContract.Presenter {


    }


    /**
     * 同上。。。
     */
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
