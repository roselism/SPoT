package com.roselism.spot.model.dao;


/**
 * Created by simon on 16-4-19.
 *
 * @param <T> 被操作的数据的类型
 */
public interface Operater<T> {

    /**
     * @param strategyContext 操作测略包
     * @param listener        监听器
     */
    void operate(StrategyContext strategyContext, OnOperateListener<T> listener);
}