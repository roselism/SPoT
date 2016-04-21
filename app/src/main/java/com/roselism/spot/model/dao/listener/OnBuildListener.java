package com.roselism.spot.model.dao.listener;

import com.roselism.spot.model.OnOperateListener;

/**
 * 创建对象时调用的接口,如果成功，就返回创建的对象，如果创建失败，则返回null
 * Created by simon on 2016/4/13.
 *
 * @deprecated 请使用统一接口OnOperateListener 替代
 */
public interface OnBuildListener<T> extends OnOperateListener {
    /**
     * 不论成功还是失败都会回掉本方法
     *
     * @param t 成功则返回build的对象，失败则返回null
     */
    public void onBuildFinished(T t);

}