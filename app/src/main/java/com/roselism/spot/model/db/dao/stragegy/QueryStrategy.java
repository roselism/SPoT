package com.roselism.spot.model.db.dao.stragegy;

import com.roselism.spot.model.db.dao.listener.OnLoadListener;

/**
 * 查询测略
 * 请在这个接口的子类中实现具体的查询操作
 * Created by simon on 16-4-18.
 * <p>
 *
 * @deprecated 实现子类请直接实现Strategy
 */
public interface QueryStrategy<T> {
    void query(OnLoadListener<T> listener);
}