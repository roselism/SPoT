package com.roselism.spot.model.engine;

import com.roselism.spot.model.db.dao.stragegy.QueryStrategy;

import java.util.List;

/**
 * 测略的容器，用于盛放各种类型的测略
 * Created by simon on 16-4-18.
 *
 * @param <R> 是OnOperateListener的某个子类
 * @param <T> 是要被操作的数据的类型（比如要被删除的数据的类型）
 */
public class StrategyContext<R extends OnOperateListener<T>, T> {
    List<QueryStrategy<T>> strategies;
    //    QueryStrategy queryStrategy;
    Strategy<R> strategy;

    public StrategyContext(Strategy strategy) {
//        this.queryStrategy = queryStrategy;
        this.strategy = strategy;
    }

    /**
     * 执行操作
     *
     * @param listener 操作的监听器
     */
    public void Do(R listener) {
//        queryStrategy.query(listener);
        strategy.Do(listener);
    }
}