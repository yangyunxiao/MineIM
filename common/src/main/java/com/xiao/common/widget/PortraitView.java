package com.xiao.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.xiao.common.R;
import com.xiao.common.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 自定义头像
 * Created by xiao on 2018/6/5.
 */

public class PortraitView extends CircleImageView {

    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    public void setup(RequestManager manager, Author author) {
//        if (author == null)
//            return;
//
//        // 进行显示
//        setup(manager, author.getPortrait());
//    }
//
//
//    public void setup(RequestManager manager, String url) {
//        setup(manager, R.mipmap.default_portrait, url);
//    }


    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null)
            url = "";
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate() // CircleImageView 控件中不能使用渐变动画，会导致显示延迟
                .into(this);

    }
}
