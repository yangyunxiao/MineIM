package com.xiao.factory.data.group;

import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 * Created by xiao on 2018/7/30.
 *
 */

public interface GroupCenter {

    /**
     * 群卡片的处理
     */
    void dispatch(GroupCard... cards);

    /**
     * 群成员的处理
     */
    void dispatch(GroupMemberCard... cards);
}
