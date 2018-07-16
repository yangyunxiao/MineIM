package com.xiao.factory;

import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.xiao.common.app.Application;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.data.user.UserCenter;
import com.xiao.factory.data.user.UserDispatcher;
import com.xiao.factory.model.api.RspModel;
import com.xiao.factory.persisitence.Account;
import com.xiao.factory.utils.DbFlowExclusionStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xiao on 2018/6/8.
 */

public class Factory {

    private static final Factory instance;

    private final Executor mExecutor;

    //全局的Gson
    private final Gson mGson;

    static {
        instance = new Factory();
    }

    private Factory() {
        //新建一个4线程的线程池
        mExecutor = Executors.newFixedThreadPool(4);

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .setExclusionStrategies(new DbFlowExclusionStrategy())
                .create();
    }

    /**
     * 初始化操作
     */
    public static void setup() {

        FlowManager.init(new FlowConfig.Builder(application())
                .openDatabasesOnInit(true)
                .build());

        //持久化的数据进行初始化
        Account.load(application());

    }


    public static void runOnAsync(Runnable runnable) {
        instance.mExecutor.execute(runnable);
    }


    public static Application application() {

        return Application.getInstance();
    }

    /**
     * 返回一个全局的Gson 这里可以添加gson初始化的配置条件
     */
    public static Gson getGson() {

        return instance.mGson;
    }

    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {

        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
//                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null) {

            callback.onDataLoadFailed(resId);

        }
    }


    public static void dispatchPush(String message) {
    }


    /**
     * 获取用户中心的实现类
     */
    public static UserCenter getUserCenter() {

        return UserDispatcher.instance();
    }
}
