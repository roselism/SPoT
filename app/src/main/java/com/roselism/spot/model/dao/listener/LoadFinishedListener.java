package com.roselism.spot.model.dao.listener;

import java.util.List;

/**
 * Created by simon on 2016/4/13.
 */
public interface LoadFinishedListener<T> {
    public void onLoadFinished(List<T> data);
}