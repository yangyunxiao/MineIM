package com.xiao.factory.presenter.group;

import android.arch.lifecycle.ViewModel;

import com.xiao.common.factory.model.Author;
import com.xiao.common.factory.presenter.BaseContract;

/**
 * 群创建的契约
 * Created by xiao on 2018/8/2.
 */

public interface GroupCreateContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 创建群
         */
        void create(String name, String desc, String picture);

        /**
         * 更改一个Model的选中状态
         */
        void changeSelect(ViewModel model, boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<Presenter, ViewModel> {
        /**
         * 创建群成功
         */
        void onCreateSucceed();
    }

    class ViewModel {
        /**
         * 用户的信息
         */
        public Author author;

        /**
         * 是否选中
         */
        public boolean isSelected;

    }
}
