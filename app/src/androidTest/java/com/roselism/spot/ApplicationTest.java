package com.roselism.spot;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.roselism.spot.model.dao.operator.PhotoOperater;
import com.roselism.spot.util.ConfUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        Math.abs(3.165);
    }
}