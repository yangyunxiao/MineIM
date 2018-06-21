package com.xiao.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.xiao.common.R;

public abstract class ToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();

        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    private void initToolbar(Toolbar toolbar) {

        mToolbar = toolbar;
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }

        initTitleNeedBack();
    }

    private void initTitleNeedBack() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
