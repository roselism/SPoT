package com.roselism.spot.model.dao.operator;

import com.roselism.spot.model.dao.listener.OnLoadListener;
import com.roselism.spot.model.dao.stragegy.QueryStrategyPackage;

/**
 * Created by simon on 16-4-18.
 */
public interface QueryOperater {
    /**
     * 查询
     *
     * @param queryStrategyPackage 测略包
     * @param listener             查询监听器
     */
    void query(QueryStrategyPackage queryStrategyPackage, OnLoadListener listener);
}