package com.roselism.spot.model.dao.operator;

import com.roselism.spot.model.dao.OnOperateListener;
import com.roselism.spot.model.dao.StrategyContext;
import com.roselism.spot.model.domain.hyper.Deleteable;

/**
 * 删除操作者
 * Created by simon on 16-4-18.
 */
public interface DeleteOperater {

    /**
     * 删除某一些元素
     *
     * @param listener 删除操作的监听器
     */
    void delete(StrategyContext strategyContext, OnOperateListener<Deleteable> listener);
}