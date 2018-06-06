package com.xiao.mineim.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.xiao.common.app.BaseFragment;

/**
 * Fragment 操作的区别
 * Add ： 往容器当中新添加一个Fragment
 * Replace ： 替换容器当中已经存在的Fragment
 * Hide/Show：纯粹的隐藏和显示，不做移除操作 仅仅操作View对象的visibility true/false
 * Attach/Detach：从布局上移除，但是仍然存储在缓存队列当中，不会被测量，但是可以被重用，可减少fragment的重复创建，减少内存抖动
 * Remove：直接移除掉，且不缓存
 * <p>
 * <p>
 * Fragment 切换帮助类
 * Created by xiao on 2018/6/6.
 */

public class NavigationHelper<T> {

    private SparseArray<Tab<T>> mTabs = new SparseArray<>();

    /**
     * 当前被选中的tab
     */
    private Tab<T> mCurrentTab;

    private final Context mContext;

    private final FragmentManager mFragmentManager;

    private final int mContainerId;

    private final OnTabChangedListener<T> mOnTabChangedListener;

    public NavigationHelper(Context mContext, int mContainerId, FragmentManager mFragmentManager, OnTabChangedListener<T> mOnTabChangedListener) {
        this.mContext = mContext;
        this.mContainerId = mContainerId;

        this.mFragmentManager = mFragmentManager;
        this.mOnTabChangedListener = mOnTabChangedListener;
    }

    /**
     * 执行点击菜单的操作
     *
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuId) {

        Tab<T> tab = mTabs.get(menuId);

        if (tab != null) {
            preSelectTab(tab);
        }

        return false;
    }

    /**
     * tab切换之前检测动作
     */
    private void preSelectTab(Tab<T> selectTab) {

        Tab<T> oldTab = null;

        if (mCurrentTab != null) {

            oldTab = mCurrentTab;

            if (oldTab == selectTab) {

                //重复点击此tab 不做添加处理  可做刷新处理
                notifyTabReselect(selectTab);

                return;

            }

        }

        mCurrentTab = selectTab;

        doSelectTab(mCurrentTab, oldTab);
    }


    private void doSelectTab(Tab<T> currentTab, Tab<T> oldTab) {

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldTab != null) {
            if (oldTab.mFragment != null) {
                //从界面中移除 但是还在内容中缓存
                fragmentTransaction.detach(oldTab.mFragment);
            }
        }

        if (currentTab != null) {

            if (currentTab.mFragment == null) {

                //首次创建
                Fragment fragment = Fragment.instantiate(mContext, currentTab.mTabClass.getName(), null);

                //缓存
                currentTab.mFragment = fragment;

                //提交到FragmentManager
                fragmentTransaction.add(mContainerId, fragment, currentTab.mTabClass.getName());

            } else {

                fragmentTransaction.attach(currentTab.mFragment);

            }

        }

        fragmentTransaction.commit();

        notifyTabSelect(currentTab, oldTab);
    }


    private void notifyTabSelect(Tab<T> currentTab, Tab<T> oldTab) {


        if (mOnTabChangedListener != null) {
            mOnTabChangedListener.onTabChanged(currentTab, oldTab);
        }
    }


    /**
     * 通知对应的tab做重复点击操作  例如刷新内容
     */
    private void notifyTabReselect(Tab<T> selectTab) {

    }

    /**
     * 添加tab
     */
    public NavigationHelper<T> addTab(int menuId, Tab<T> tab) {

        this.mTabs.put(menuId, tab);

        return this;
    }

    /**
     * 获取当前正在显示的tab
     */
    public Tab<T> getCurrentTab() {

        return mCurrentTab;
    }


    public static class Tab<T> {

        //Fragment对应的class信息
        public Class<? extends BaseFragment> mTabClass;

        //额外的字段
        public T mExtra;

        public Tab(Class<? extends BaseFragment> tabClass, T extra) {
            this.mTabClass = tabClass;
            this.mExtra = extra;
        }

        //缓存fragment
        Fragment mFragment;
    }


    public interface OnTabChangedListener<T> {

        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
