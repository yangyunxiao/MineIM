package com.xiao.factory.data.user;

import android.text.TextUtils;

import com.xiao.factory.data.helper.DbHelper;
import com.xiao.factory.model.card.UserCard;
import com.xiao.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */

public class UserDispatcher implements UserCenter {

    private static UserCenter instance;

    /**
     * 单线程池,处理消息
     */
    private final Executor executor = Executors.newSingleThreadExecutor();

    private UserDispatcher() {

    }

    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }

        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {

        if (cards == null || cards.length == 0) {
            return;
        }

        executor.execute(new UserCardHandler(cards));
    }

    /**
     *
     */
    private class UserCardHandler implements Runnable {

        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {

            this.cards = cards;
        }

        @Override
        public void run() {

            List<User> users = new ArrayList<>();

            for (UserCard card : cards) {
                //进行过滤操作 用户没有id的过滤掉
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.build());
            }

            //异步进行 进行数据库的存储 ， 并分发通知
            DbHelper.save(User.class, users.toArray(new User[0]));

        }
    }
}
