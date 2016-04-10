package com.roselism.spot.library.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;

import com.gordonwong.materialsheetfab.AnimatedFab;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by hero2 on 2016/2/15.
 */
public class MenuActionButton extends FloatingActionButton implements AnimatedFab {
    public MenuActionButton(Context context) {
        super(context);
    }

    public MenuActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void show(float translationX, float translationY) {

    }
}
