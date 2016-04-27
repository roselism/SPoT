package com.roselism.spot.model.engine.bmob;


import com.roselism.spot.model.engine.OnOperateListener;
import com.roselism.spot.model.engine.Operater;
import com.roselism.spot.model.engine.StrategyContext;

/**
 * 对于bmob对象的操作者
 * Created by simon on 16-4-18.
 *
 * @param <T> 要被操作的类型, 继承自bmobobject对象
 */
public class BmobOperater<T> implements Operater<T> {

    /**
     * 执行一些操作
     *
     * @param strategyContext 操作测略包
     * @param listener        监听器
     */
    @Override
    public void operate(StrategyContext strategyContext, OnOperateListener<T> listener) {
        strategyContext.Do(listener);
    }
}