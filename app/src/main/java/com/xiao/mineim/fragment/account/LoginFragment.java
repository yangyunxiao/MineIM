package com.xiao.mineim.fragment.account;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiao.common.app.BaseFragment;
import com.xiao.mineim.R;

public class LoginFragment extends BaseFragment {

    private IAccountTrigger mAccountTrigger;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (IAccountTrigger) context;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAccountTrigger.triggerView();
    }
}
