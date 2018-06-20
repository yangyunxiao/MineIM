package com.xiao.mineim.fragment.account;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.app.ViewFragment;
import com.xiao.factory.presenter.account.LoginContract;
import com.xiao.factory.presenter.account.LoginPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends ViewFragment<LoginContract.Presenter>
        implements LoginContract.View {

    @BindView(R.id.login_edit_phone)
    EditText mPhone;

    @BindView(R.id.login_edit_password)
    EditText mPassword;

    @BindView(R.id.login_loading)
    Loading mLoading;

    @BindView(R.id.login_button_submit)
    Button mSubmit;


    private IAccountTrigger mAccountTrigger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (IAccountTrigger) context;
    }

    protected int getContentLayoutID() {
        return R.layout.fragment_login;
    }


    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAccountTrigger.triggerView();
    }

    @OnClick(R.id.login_button_submit)
    void onSubmitClick() {

        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();

        mPresenter.login(phone, password);
    }

    @OnClick(R.id.login_text_register)
    void onShowRegisterClick() {
        //切换界面
        mAccountTrigger.triggerView();

    }

    @Override
    public void showError(@StringRes int error) {
        super.showError(error);

        mLoading.stop();
        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {

        MainActivity.show(getContext());

        getActivity().finish();
    }
}
