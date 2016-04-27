package com.roselism.spot.model.db.dao.listener;

import com.roselism.spot.model.engine.OnOperateListener;

/**
 * 删除操作监听器
 * Created by simon on 2016/4/15.
 *
 * @deprecated 请使用统一接口OnOperateListener 替代
 */
public interface OnDeleteListener<T> extends OnOperateListener<T> {

    /**
     * 当删除完成时回掉
     *
     * @param t 被删除的对象
     */
    void onDeleteFinished(T t);
}