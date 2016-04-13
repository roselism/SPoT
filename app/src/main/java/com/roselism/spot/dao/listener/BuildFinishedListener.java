package com.roselism.spot.dao.listener;

/**
 * 创建对象时调用的接口,如果成功，就返回创建的对象，如果创建失败，则返回null
 * Created by simon on 2016/4/13.
 */
public interface BuildFinishedListener<T> {
    public void onBuildFinished(T t);
}