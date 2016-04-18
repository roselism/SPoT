package com.roselism.spot.model.dao.listener;

import cn.bmob.v3.BmobObject;

/**
 * 修改监听器
 * Created by simon on 2016/4/15.
 */
public interface OnUpdateListener<T extends BmobObject> {
    void onUpdateFinished(T t);
}

