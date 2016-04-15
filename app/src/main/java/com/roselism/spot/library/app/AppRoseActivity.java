package com.roselism.spot.library.app;

import android.content.Context;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by hero2 on 2016/2/19.
 */
public abstract class AppRoseActivity extends AppCompatActivity {
//
//    /**
//     * 初始化所有点击监听器
//     */
//    protected abstract void initAllListener();

    /**
     * 获取当前的的用户
     *
     * @param <T>
     * @return
     */
    protected abstract <T extends BmobUser> T getUser();

    protected Context getOutterClass() {
        return this;
    }

}