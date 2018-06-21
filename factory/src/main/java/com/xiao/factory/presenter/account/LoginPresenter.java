package com.xiao.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.xiao.common.factory.data.DataSource;
import com.xiao.factory.R;
import com.xiao.factory.data.helper.AccountHelper;
import com.xiao.factory.model.api.account.LoginModel;
import com.xiao.factory.model.db.User;
import com.xiao.factory.persisitence.Account;
import com.xiao.common.factory.presenter.BasePresenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 * 登录界面presenter
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter, DataSource.Callback<User> {

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();

        LoginContract.View view = getView();

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {

            view.showError(R.string.data_account_login_invalid_parameter);

        } else {

            LoginModel loginModel = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(loginModel, this);

        }

    }

    @Override
    public void onDataLoadSuccess(User user) {

        final LoginContract.View view = getView();

        if (view == null) {

            return;
        }

        //线程切换  主线程中刷新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataLoadFailed(@StringRes final int failedMsg) {

        final LoginContract.View view = getView();

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
