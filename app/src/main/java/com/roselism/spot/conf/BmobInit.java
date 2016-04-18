package com.roselism.spot.conf;

import android.content.Context;

import com.roselism.spot.util.ConfUtil;

import cn.bmob.v3.Bmob;

/**
 * 初始化bmob对象
 * Created by simon on 2016/4/18.
 */
public class BmobInit {
    Context mContext;

    public BmobInit(Context context) {
        this.mContext = context;
    }

    void initBmob() {
        Bmob.initialize(mContext, ConfUtil.getBmobAppId());
    }
}