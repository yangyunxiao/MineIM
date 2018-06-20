package com.xiao.mineim.fragment.account;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xiao.common.app.BaseFragment;
import com.xiao.common.app.ViewFragment;
import com.xiao.factory.presenter.account.RegisterContract;
import com.xiao.factory.presenter.account.RegisterPresenter;
import com.xiao.mineim.R;
import com.xiao.mineim.activity.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends ViewFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    private static final String TAG = RegisterFragment.class.getName();

    private IAccountTrigger mAccountTrigger;

    @BindView(R.id.register_edit_phone)
    EditText mPhone;
    @BindView(R.id.register_edit_password)
    EditText mPassword;
    @BindView(R.id.register_edit_name)
    EditText mName;

    @BindView(R.id.register_loading)
    Loading mLoading;

    @BindView(R.id.register_button_submit)
    Button mSubmit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (IAccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    public RegisterFragment() {

    }

    @OnClick(R.id.register_button_submit)
    void onRegisterClick() {

        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();

        mPresenter.register(phone, name, password);

    }

    @OnClick(R.id.register_text_login)
    void onLoginClick() {

        mAccountTrigger.triggerView();

        Log.e(TAG, "triggerView");

    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();

        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mName.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void showError(@StringRes int error) {
        super.showError(error);
        mLoading.stop();

        mPhone.setEnabled(true);
        mPassword.setEnabled(true);
        mName.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_register;
    }

    @Override
    public void registerSuccess() {

        //注册成功  此时用户已成功登录  跳转到主界面
        MainActivity.show(getActivity());
        getActivity().finish();

    }
}
