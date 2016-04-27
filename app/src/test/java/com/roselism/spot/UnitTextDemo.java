package com.roselism.spot;

import android.test.AndroidTestCase;

import com.roselism.spot.util.ConfUtil;
import com.roselism.spot.util.DesityUtil;


/**
 * Created by hero2 on 2016/3/7.
 */
public class UnitTextDemo extends AndroidTestCase {
    public void test() {
        DesityUtil.dp2px(getContext(), 56);
    }

    //a736bff2e503810b1e7e68b248ff5a7d
    public void testGetBmobAppId() {
        assertEquals("a736bff2e503810b1e7e68b248ff5a7d", ConfUtil.getBmobAppId());
    }
}
