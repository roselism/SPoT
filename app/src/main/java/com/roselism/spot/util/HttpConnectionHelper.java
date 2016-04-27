package com.roselism.spot.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by simon on 2016/4/26.
 *
 * @version 1.0
 */
public class HttpConnectionHelper {

    /**
     * 网络请求的方式
     */
    public static final String POST_METHOD = "POST";

    /**
     * 网络连接get方式
     */
    public static final String GET_METHOD = "GET";

    /**
     * 网络连接的地址
     * 就是url，为了避免变量命名的重复，所以就更名为了path
     */
    private String path;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 是否使用缓存
     */
    private boolean useCatches;

    /**
     * 设置连接超时
     */
    private int connectionTimeOut;

    /**
     * 设置读取超时
     */
    private int readTimeOut;

    /**
     * 建造者模式
     *
     * @param builder 构造器
     */
    private HttpConnectionHelper(Builder builder) {
        this.path = builder.path;
        this.requestMethod = builder.requestMethod;
        this.useCatches = builder.useCatches;
        this.connectionTimeOut = builder.connectionTimeOut;
        this.readTimeOut = builder.readTimeOut;
    }

    /**
     * @return 返回<code>HttpURLConnection</code>对象
     * @throws IOException
     * @since 1.0
     */
    public HttpURLConnection getConnection() throws IOException {
        if (path == null)
            throw new IllegalArgumentException("网络路径不能为null");
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // 打开连接
//        roselismImager();
        return connection;
    }

    /**
     * 获取响应码
     *
     * @return
     * @throws IOException
     */
    public int responseCode() throws IOException {
        return getConnection().getResponseCode();
    }

    /**
     * 是否响应ok（响应码是否为200）
     *
     * @return 如果响应码为200则返回true，反之返回false
     * @throws IOException
     */
    public boolean isResponseOk() throws IOException {

        return responseCode() == 200;
    }


    /**
     * 建造者
     *
     * @since 1.0
     */
    public static class Builder {
        public String path;
        public String requestMethod = POST_METHOD; // 请求方法
        public boolean useCatches = false; // 使用缓存
        public int connectionTimeOut = 5000; // 连接超时
        public int readTimeOut = 5000;

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        /**
         * 默认为post方法
         *
         * @param requestMethod
         * @return
         */
        public Builder setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        /**
         * 默认为false
         *
         * @param useCatches
         * @return
         */
        public Builder setUseCatches(boolean useCatches) {
            this.useCatches = useCatches;
            return this;
        }

        /**
         * 设置连接超时
         * 默认为5000ms
         *
         * @param connectionTimeOut 连接超时值 单位为ms
         * @return
         */
        public Builder setConnectionTimeOut(int connectionTimeOut) {
            this.connectionTimeOut = connectionTimeOut;
            return this;
        }

        /**
         * 设置请求超时
         * 默认为5000ms
         *
         * @param readTimeOut 单位为ms
         * @return
         */
        public Builder setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public HttpConnectionHelper build() {
            return new HttpConnectionHelper(this);
        }
    }
}