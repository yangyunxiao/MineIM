package com.xiao.common.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiao.common.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    protected Unbinder mRootUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        initArgs(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRootView == null) {

            int layoutID = getContentLayoutID();

            mRootView = inflater.inflate(layoutID, container, false);

            initWidget(mRootView);

        } else {

            if (mRootView.getParent() != null) {
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);

            }
        }

        return mRootView;
    }

    /**
     * View创建完成之后初始化数据
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected void initArgs(Bundle bundle) {
    }

    protected abstract int getContentLayoutID();

    protected void initWidget(View rootView) {

        mRootUnBinder = ButterKnife.bind(this, rootView);
    }

    protected void initData() {

    }

    protected boolean onBackPressed() {

        return false;
    }

}
