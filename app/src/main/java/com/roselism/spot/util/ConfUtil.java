package com.roselism.spot.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * 配置读取工具
 * Created by simon on 2016/4/18.
 */
public class ConfUtil {

    /**
     * 返回bmob的id
     *
     * @return
     */
    public static String getBmobAppId() {
        File bmobFile = new File("src\\main\\res\\conf\\bmob.txt");
        String id = "";
        try {
            Reader reader = new FileReader(bmobFile);
            char[] chars = new char[512];
            while (reader.read(chars) != -1) {
                id += String.valueOf(chars);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
}