package com.roselism.spot;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by simon on 2016/4/18.
 */
public class ConfUtil {

    @Test
    public void testGetBmobAppId() {
//        assertEquals("a736bff2e503810b1e7e68b248ff5a7d", com.roselism.spot.util.ConfUtil.getBmobAppId());
//        assertEquals("a736bff2e503810b1e7e68b248ff5a7d", com.roselism.spot.util.ConfUtil.getBmobAppId());
        assertNotNull(com.roselism.spot.util.ConfUtil.getBmobAppId());
    }
}