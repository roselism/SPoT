package com.roselism.spot;

import android.test.AndroidTestCase;

import com.roselism.spot.util.DesityUtils;


/**
 * Created by hero2 on 2016/3/7.
 */
public class UnitTextDemo extends AndroidTestCase {
    public void test() {
        DesityUtils.dp2px(getContext(), 56);
    }

}
