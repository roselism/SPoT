package com.roselism.spot.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.roselism.spot.model.domain.Config;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * 配置读取工具
 * Created by simon on 2016/4/18.
 */
public class ConfUtil {


    public static List<Config> xml2List() throws XmlPullParserException, FileNotFoundException {

        List<Config> configs = Lists.newArrayList();
        File in = new File("src\\main\\res\\conf\\AppConfig.xml");

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(Files.newReader(in, Charsets.UTF_8));

        int type = parser.getEventType();
        Config config = null;
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {
                if ("appName".equals(parser.getName())) { // 进入bmob标签中
                    config = new Config();
                } else if ("applicationId".equals(parser.getName())) {
                    config.setId(parser.getText());
                } else if ("appName".equals(parser.getName())) {
                    config.setName(parser.getText());
                }
            } else { // end tag
                configs.add(config);
            }
        }

        return configs;
    }

    /**
     * 返回bmob的id
     *
     * @return
     */
    public static String getBmobAppId() {

        String id = "";
        try {
            List<Config> list = xml2List();
            for (Config config : list) {
                if ("bmob".equals(config.getName())) {
                    id = config.getId();
                    break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return id;
    }
}