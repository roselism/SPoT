package com.roselism.spot.model.db.dao.operator;

import com.roselism.spot.model.engine.StrategyContext;
import com.roselism.spot.model.db.dao.listener.OnLoadListener;

/**
 * Created by simon on 16-4-18.
 *
 * @deprecated 请使用超类Operater代替
 */
public interface QueryOperater {
    /**
     * 查询
     *
     * @param strategyContext 测略包
     * @param listener        查询监听器
     */
    void query(StrategyContext strategyContext, OnLoadListener listener);
}