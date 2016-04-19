package com.roselism.spot;


import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Created by simon on 2016/4/18.
 */
public class ConfUtilTest {

    @Test
    public void testGetBmobAppId() {
        try {
            assertNotNull(com.roselism.spot.util.ConfUtil.xml2List().size());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}