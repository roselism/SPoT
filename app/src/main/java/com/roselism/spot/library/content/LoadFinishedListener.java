package com.roselism.spot.library.content;

import java.util.List;

/**
 * Created by simon on 2016/4/13.
 */
public interface LoadFinishedListener<T> {
    public void onLoadFinished(List<T> data);
}