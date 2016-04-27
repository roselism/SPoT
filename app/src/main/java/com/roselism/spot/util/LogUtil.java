package com.roselism.spot.util;

import android.util.Log;

/**
 * @version 1.0
 * @创建者 lai
 * @创建时间 2016/4/10
 * @packageName com.roselism.spot.util
 * @更新时间 2016/4/10 15:45
 * @描述 Log日志管理
 */

public class LogUtil {


    /**
     * @since 1.0
     */
    private static final String APPTAG = "SPoT";

    private static boolean isDebug = true;

    /**
     * @since 1.0
     */
    private LogUtil() {

    }


    /**
     * 打印log.d日志
     * 默认的TAG 是 SPoT
     *
     * @param message 打印的信息
     */
    public static void d(String message) {
        if (isDebug)
            d(APPTAG, message);
    }

    /**
     * 打印log.d日志
     *
     * @param message 打印的信息
     */
    public static void i(String message) {
        if (isDebug)
            Log.i(APPTAG, message);
    }

    /**
     * 打印log.d日志
     *
     * @param tag
     * @param message 打印的信息
     */
    public static void d(String tag, String message) {
        if (isDebug)
            Log.d(tag, message);
    }

    /**
     * 打印log.i日志
     *
     * @param tag
     * @param message 打印的信息
     */
    public static void i(String tag, String message) {
        if (isDebug)
            Log.i(tag, message);
    }

    /**
     * 是否是调试模式
     *
     * @param isDebug 如果是调试模式则为true 反之false
     * @author Simon
     * @since 1.1
     */
    public static void setIsDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    /**
     * @return 如果当前为debug模式则返回true
     * @author Simon Wang
     * @since 1.1
     */
    public static boolean isDebug() {
        return isDebug;
    }
}