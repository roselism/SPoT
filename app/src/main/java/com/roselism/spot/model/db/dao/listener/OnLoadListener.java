package com.roselism.spot.model.db.dao.listener;

import java.util.List;

/**
 * Created by simon on 16-4-16.
 * @deprecated 请使用统一接口OnOperateListener 替代
 */
public interface OnLoadListener<T> {

    void onLoadFinished(List<T> data);
}