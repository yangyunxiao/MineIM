package com.xiao.common.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * UI相关工具类
 * Created by xiao on 2018/8/10.
 */

public class UiTool {

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
