package com.roselism.spot.model.dao.stragegy;

import com.roselism.spot.model.dao.listener.OnLoadListener;

/**
 * Created by simon on 16-4-18.
 */
public interface QueryStrategy {
    void query(OnLoadListener listener);
}