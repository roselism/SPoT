package com.roselism.spot;

import android.app.Application;
import android.content.Context;
import android.os.*;

import com.roselism.spot.util.LogUtils;

/**
 * @创建者 lai
 * @创建时间 2016/4/10
 * @packageName com.roselism.spot
 * @更新时间 2016/4/10 15:33
 * @描述 TODO
 */
public class SPoTApplication extends Application {
    private static Handler sMainHandler = new Handler();

    private static Context sContext;// Application的上下文
    private static int sMainThreadId;// 主线程Handler

    /**
     * 获取context
     *
     * @return application的context
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * 获取主线程ID
     *
     * @return 主线程ID
     */
    public static int getMainThreadId() {
        return sMainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.i("SPoTApplication", "onCreate");

        sContext = getApplicationContext();
        sMainThreadId = android.os.Process.myTid();

        LogUtils.setIsDebug(true); // 开启debug模式
    }

    public static Handler getMainHandler() {
        return sMainHandler;
    }
}