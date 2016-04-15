package com.roselism.spot.model.dao.listener;

/**
 * 删除操作监听器
 * Created by simon on 2016/4/15.
 */
public interface DeleteListener<T> {

    /**
     * 当删除完成时回掉
     *
     * @param t 被删除的对象
     */
    void onDeleteFinished(T t);
}