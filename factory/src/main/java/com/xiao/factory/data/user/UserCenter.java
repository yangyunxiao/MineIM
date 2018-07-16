package com.xiao.factory.data.user;

import com.xiao.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 */

public interface UserCenter {

    /**
     * 分发处理用户卡片你的信息，并更新到数据库
     */
    void dispatch(UserCard... cards);
}
