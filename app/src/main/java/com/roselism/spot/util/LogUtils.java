package com.roselism.spot.util;

import android.util.Log;

/**
 * @创建者 lai
 * @创建时间 2016/4/10
 * @packageName com.roselism.spot.util
 * @更新时间 2016/4/10 15:45
 * @描述 Log日志管理
 */
public class LogUtils {
	private boolean isDebug;

	/**
	 * 打印log.d日志
	 * 
	 * @param message
	 *            打印的信息
	 */
	public static void d(String message) {
		d("SPoT", message);
	}
	/**
	 * 打印log.d日志
	 * 
	 * @param message
	 *            打印的信息
	 */
	public static void i(String message) {
		i("SPoT", message);
	}

	/**
	 * 打印log.d日志
	 * 
	 * @param tag
	 * @param message
	 *            打印的信息
	 */
	public static void d(String tag, String message) {
		Log.d(tag, message);
	}

	/**
	 * 打印log.i日志
	 * 
	 * @param tag
	 * @param message
	 *            打印的信息
	 */
	public static void i(String tag, String message) {
		Log.i(tag, message);
	}
}
