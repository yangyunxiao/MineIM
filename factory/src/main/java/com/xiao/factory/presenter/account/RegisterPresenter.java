package com.xiao.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.xiao.common.Common;
import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.R;
import com.xiao.factory.data.helper.AccountHelper;
import com.xiao.factory.model.api.account.RegisterModel;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;
import com.xiao.common.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {

    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {

        start();

        RegisterContract.View view = getView();

        if (!checkMobile(phone)) {
            //手机号码错误
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            view.showError(R.string.data_account_register_invalid_parameter_name);

        } else if (password.length() < 6) {
            view.showError(R.string.data_account_register_invalid_parameter_password);

        } else {

            RegisterModel registerModel = new RegisterModel(phone, password, name, Account.getPushId());
            //进行网络请求，并设置回调接口为自己
            AccountHelper.register(registerModel, this);

        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoadSuccess(User user) {

        final RegisterContract.View view = getView();

        if (view == null) {

            return;
        }

        //线程切换  主线程中刷新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });

    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {

        final RegisterContract.View view = getView();

        if (view == null) {

            return;
        }

        //线程切换  主线程中刷新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(failedMsg);
            }
        });
    }
}
