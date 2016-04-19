package com.roselism.spot.model.dao.operator;

import com.roselism.spot.model.dao.StrategyContext;
import com.roselism.spot.model.dao.listener.OnLoadListener;

/**
 * Created by simon on 16-4-18.
 */
public interface QueryOperater {
    /**
     * 查询
     *
     * @param strategyContext 测略包
     * @param listener             查询监听器
     */
    void query(StrategyContext strategyContext, OnLoadListener listener);
}