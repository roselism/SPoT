package com.roselism.spot.model.dao.stragegy;

import com.roselism.spot.model.dao.listener.OnLoadListener;

/**
 * Created by simon on 16-4-18.
 */
public class QueryStrategyPackage {
    QueryStrategy queryStrategy;

    public QueryStrategyPackage(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

    public void query(OnLoadListener listener) {
        queryStrategy.query(listener);
    }
}
