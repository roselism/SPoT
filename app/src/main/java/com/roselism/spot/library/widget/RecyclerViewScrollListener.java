package com.roselism.spot.library.widget;

import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView 的滑动监听器
 * <p/>
 * Created by hero2 on 2016/3/14.
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int mScrollThreshold;

    /**
     * 向上滑动（内容由下至上）
     */
    public abstract void onScrollUp();

    /**
     * 向下滑动（内容有上至下）
     */
    public abstract void onScrollDown();

    public RecyclerViewScrollListener(int mScrollThreshold) {
        this.mScrollThreshold = mScrollThreshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta) {
            if (dy > 0) {
                onScrollDown();
            } else {
                onScrollUp();
            }
        }
    }

    /**
     * 设置触发滑动距离
     *
     * @param scrollThreshold
     */
    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
