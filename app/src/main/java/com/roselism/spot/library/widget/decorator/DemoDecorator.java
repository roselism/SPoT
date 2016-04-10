package com.roselism.spot.library.widget.decorator;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hero2 on 2016/3/12.
 */
public class DemoDecorator extends RecyclerView.ItemDecoration {

    public DemoDecorator() {
        super();
    }

    /**
     * onDraw方法先于drawChildren
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    /**
     * onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        view.setPadding(outRect.left, outRect.top, outRect.right, outRect.bottom);
    }
}