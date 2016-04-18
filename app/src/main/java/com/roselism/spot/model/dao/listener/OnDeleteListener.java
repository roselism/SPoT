package com.roselism.spot.model.dao.listener;

import cn.bmob.v3.BmobObject;

/**
 * 删除操作监听器
 * Created by simon on 2016/4/15.
 */
public interface OnDeleteListener<T> {

    /**
     * 当删除完成时回掉
     *
     * @param t 被删除的对象
     */
    void onDeleteFinished(T t);
}