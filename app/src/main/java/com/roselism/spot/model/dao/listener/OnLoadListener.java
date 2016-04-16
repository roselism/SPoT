package com.roselism.spot.model.dao.listener;

import java.util.List;

/**
 * Created by simon on 16-4-16.
 */
public interface OnLoadListener<T> {

    void onLoadFinished(List<T> data);
}
