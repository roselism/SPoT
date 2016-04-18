package com.roselism.spot.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @创建者 lai
 * @创建时间 2016/4/10
 * @packageName com.roselism.spot.util
 * @更新时间 2016/4/10 15:28
 * @描述 屏幕适配像素转换
 * 转换公式
 * px=dp*(dpi/160)
 * pd=px*160/dpi
 */
public class DesityUtil {
    /**
     * dp转成px
     *
     * @param dp dp
     *
     * @return 像素(px)
     */
    public static int dp2px(Context context, int dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return dp * dpi / 160;
    }

    /**
     * px转成dp
     *
     * @param px px
     *
     * @return 像素(dp)
     */
    public static int px2dp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return px * 160 / dpi;
    }

}
