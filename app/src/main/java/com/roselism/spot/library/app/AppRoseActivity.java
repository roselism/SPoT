package com.roselism.spot.library.app;

<<<<<<< HEAD
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.BmobUser;

=======
import android.support.v7.app.AppCompatActivity;

>>>>>>> 032282a... init
/**
 * Created by hero2 on 2016/2/19.
 */
public abstract class AppRoseActivity extends AppCompatActivity {
<<<<<<< HEAD
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
=======

    /**
     * 初始化所有点击监听器
     */
    protected abstract void initAllListener();
>>>>>>> 032282a... init
}