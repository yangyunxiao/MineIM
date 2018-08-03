package com.xiao.factory.persisitence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xiao.factory.Factory;
import com.xiao.factory.model.api.account.AccountResponseModel;
import com.xiao.factory.model.db.User;
import com.xiao.factory.model.db.User_Table;

public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIND = "KEY_IS_BIND";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    /**
     * 设备的推送ID
     */
    private static String pushId;

    /**
     * 是否已绑定
     */
    private static boolean isBind;

    private static String userId;
    //登录状态的Token
    private static String token;

    private static String account;

    public static String getPushId() {
        return pushId;
    }

    public static void setPushId(String pushId) {
        Account.pushId = pushId;

        save(Factory.application());
    }

    public static void load(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);

        pushId = sharedPreferences.getString(KEY_PUSH_ID, "");
        isBind = sharedPreferences.getBoolean(KEY_IS_BIND, false);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        userId = sharedPreferences.getString(KEY_USER_ID, "");
        account = sharedPreferences.getString(KEY_ACCOUNT, "");

    }


    private static void save(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);

        sharedPreferences.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 保存自己的信息到XML中
     */
    public static void login(AccountResponseModel responseModel) {

        Account.token = responseModel.getToken();
        Account.account = responseModel.getAccount();
        Account.userId = responseModel.getUser().getId();
        save(Factory.application());

    }

    public static boolean isLogin() {

        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);
    }

    /**
     * 用户信息是否完善
     */
    public static boolean userInfoIsCompleted() {

        if (isLogin()) {

            User user = Account.getCurrentUser();

            return !TextUtils.isEmpty(user.getDesc())
                    && !TextUtils.isEmpty(user.getPortrait())
                    && user.getSex() != 0;
        }

        return false;

    }


    /**
     * 判断用户是否已经绑定了
     */
    public static boolean isBind() {

        return isBind;

    }

    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        save(Factory.application());
    }

    /**
     * 获取当前用户
     */
    public static User getCurrentUser() {

        //如果是null  返回一个User对象  不是null  数据库查询
        return TextUtils.isEmpty(userId) ? new User()
                : SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }


    public static String getToken() {
        return token;
    }

    /**
     * 返回当前用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {

        return getCurrentUser().getId();
    }
}
