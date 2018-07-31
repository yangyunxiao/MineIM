package com.xiao.factory.data.group;

import com.xiao.factory.data.helper.DbHelper;
import com.xiao.factory.data.helper.GroupHelper;
import com.xiao.factory.data.helper.UserHelper;
import com.xiao.factory.model.card.GroupCard;
import com.xiao.factory.model.card.GroupMemberCard;
import com.xiao.factory.model.db.Group;
import com.xiao.factory.model.db.GroupMember;
import com.xiao.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xiao on 2018/7/30.
 * 群/群成员卡片中心的实现类
 */
public class GroupDispatcher implements GroupCenter {

    private static GroupCenter instance;

    private Executor executor = Executors.newSingleThreadExecutor();

    public static GroupCenter instance() {
        if (instance == null) {
            synchronized (GroupDispatcher.class) {
                if (instance == null) {
                    instance = new GroupDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(GroupCard... cards) {

        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupHandler(cards));
    }

    @Override
    public void dispatch(GroupMemberCard... cards) {

        if (cards == null || cards.length == 0) {
            return;
        }
        executor.execute(new GroupMemberRspHandler(cards));
    }

    /**
     * 把群Card处理为群DB类
     */
    private class GroupHandler implements Runnable {

        private final GroupCard[] cards;

        public GroupHandler(GroupCard[] cards) {

            this.cards = cards;
        }

        @Override
        public void run() {

            List<Group> groups = new ArrayList<>();

            for (GroupCard card : cards) {

                User owner = UserHelper.search(card.getOwnerId());
                if (owner != null) {
                    Group group = card.build(owner);
                    groups.add(group);
                }
            }
            if (groups.size() > 0) {
                DbHelper.save(Group.class, groups.toArray(new Group[0]));
            }
        }
    }

    private class GroupMemberRspHandler implements Runnable {
        private final GroupMemberCard[] cards;

        public GroupMemberRspHandler(GroupMemberCard[] cards) {

            this.cards = cards;
        }

        @Override
        public void run() {

            List<GroupMember> members = new ArrayList<>();
            for (GroupMemberCard card : cards) {
                User user = UserHelper.search(card.getUserId());
                Group group = GroupHelper.find(card.getGroupId());

                if (user != null && group != null) {
                    GroupMember member = card.build(group, user);
                    members.add(member);
                }
            }

            if (members.size() > 0) {
                DbHelper.save(GroupMember.class, members.toArray(new GroupMember[0]));
            }
        }
    }
}
