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
        assertNotNull(com.roselism.spot.util.ConfUtil.getBmobAppId());
    }
}