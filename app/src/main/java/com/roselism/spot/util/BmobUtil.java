package com.roselism.spot.util;

import android.util.Log;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by simon on 2016/4/10.
 */
public class BmobUtil {

    /**
     * 获取当前项目的bmob密钥
     *
     * @return
     */
    public static String getApplicationId() {
        java.io.File file = new java.io.File("SPoT\\bmob.txt");
        Reader reader = null;
        String apiCode = "";
        try {
            reader = new FileReader(file);
            char[] chars = new char[64];
            while (reader.read(chars) != -1) {
                apiCode += chars.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader = null;
            }
        }
//        Log.i(TAG, "getApplicationId: -->" + apiCode);
        return apiCode;
    }
}
