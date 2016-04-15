package com.roselism.spot.model.dao.listener;

import java.util.List;

/**
 * Created by simon on 2016/4/13.
 */
public interface LoadListener<T> {

    /**
     * 不论加载成功还是加载失败都会回掉本方法
     *
     * @param data
     */
    public void onLoadFinished(List<T> data);
}