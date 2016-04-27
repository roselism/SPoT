package com.roselism.spot.model.engine;

/**
 * 操作的监听器
 * 其他所有类型操作的超类
 * <p>
 * Created by simon on 16-4-19.
 *
 * @param <T> 被操作的数据的类型
 */
public interface OnOperateListener<T> {

    /**
     * 当某项操作完成时
     *
     * @param t 被操作的类型
     */
    void onOperated(T t);
}