package com.roselism.spot.model;


/**
 * 操作对象的种种测略的超类
 * Created by simon on 16-4-18.
 *
 * @param <T> OnOperateListener 的子类
 */
public interface Strategy<T extends OnOperateListener> {
    void Do(T t);
}