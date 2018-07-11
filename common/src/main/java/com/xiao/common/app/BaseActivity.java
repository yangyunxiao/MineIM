package com.xiao.common.app;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.xiao.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected PlaceHolderView mPlaceHolderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面未初始化之前调用初始化窗口
        initWindow();
        if (initArgs(getIntent().getExtras())) {

            int layoutId = getContentLayoutID();
            setContentView(layoutId);
            initBefore();
            initWidget();
            initData();

        } else {
            finish();
        }
    }

    protected void initBefore() {

    }

    protected void initWindow() {

    }

    protected boolean initArgs(Bundle extras) {
        return true;
    }

    /**
     * 获取布局资源文件ID
     */
    protected abstract int getContentLayoutID();

    protected void initWidget() {

        ButterKnife.bind(this);

    }

    protected void initData() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        //noinspection RestrictedApi
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments != null && fragments.size() > 0) {

            for (Fragment fragment : fragments) {

                if (fragment instanceof BaseFragment) {

                    if (((BaseFragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();

        finish();
    }

    /**
     * 设置占位布局
     *
     * @param mPlaceHolderView 继承了展位布局的规范View
     */
    public void setPlaceHolderView(PlaceHolderView mPlaceHolderView) {
        this.mPlaceHolderView = mPlaceHolderView;
    }
}
