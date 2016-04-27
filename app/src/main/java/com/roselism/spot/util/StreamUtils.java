package com.roselism.spot.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by simon on 2016/4/24.
 */
public class StreamUtils {

    /**
     * @param inputStream 将输入流转换成为string对象
     * @return 返回对应的string 对象
     */
    public static String input2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1)
            outputStream.write(buffer, 0, length);

        outputStream.close();

        return outputStream.toString();
    }
}
