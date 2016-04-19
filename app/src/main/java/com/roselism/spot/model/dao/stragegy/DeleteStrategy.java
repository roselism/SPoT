package com.roselism.spot.model.dao.stragegy;

import com.roselism.spot.model.dao.Strategy;
import com.roselism.spot.model.dao.listener.OnDeleteListener;

/**
 * 删除的测略
 * 所有的对于数据的操作都应该实现该方法
 * 请在这个接口的子类中实现具体的删除操作
 * Created by simon on 16-4-18.
 *
 * @param <T> 被删除的对象的类型
 * @deprecated 实现子类请直接实现Strategy
 */
public interface DeleteStrategy<T> extends Strategy {
    void delete(OnDeleteListener<T> listener);
}