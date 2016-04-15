package com.roselism.spot.library.content;

import android.content.Context;

/**
 * 数据
 * Created by simon on 2016/4/9.
 */
public abstract class DataLoader implements Runnable {

    protected Context outerClass;
    public static final int LOAD_FINISHED = 0x16;

    /**
     * 创建一个数据加载器
     *
     * @param outerClass 上下文对象
     */
    public DataLoader(Context outerClass) {
        this.outerClass = outerClass;
    }
}