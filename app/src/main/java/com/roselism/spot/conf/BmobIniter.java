package com.roselism.spot.conf;

import android.content.Context;

import com.roselism.spot.util.ConfUtil;

import cn.bmob.v3.Bmob;

/**
 * 初始化bmob对象
 * Created by simon on 2016/4/18.
 */
public class BmobIniter {
    Context mContext;

    public BmobIniter(Context context) {
        this.mContext = context;
    }

    public void initBmob() {
        Bmob.initialize(mContext, ConfUtil.getBmobAppId());
    }
}