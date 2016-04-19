package com.roselism.spot.model.dao.listener;

import cn.bmob.v3.BmobObject;

/**
 * 修改监听器
 * Created by simon on 2016/4/15.
 *
 * @deprecated 请使用统一接口OnOperateListener 替代
 */
public interface OnUpdateListener<T> {
    void onUpdateFinished(T t);
}

