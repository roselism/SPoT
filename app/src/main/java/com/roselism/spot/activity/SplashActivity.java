package com.roselism.spot.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roselism.spot.R;
import com.roselism.spot.util.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    private static final int LOAD_FINISHED = 0;
    private static final int JSON_ERROR = 1;
    private static final int URL_ERROR = 2;
    private static final int IO_ERROR = 3;

    @Bind(R.id.versionName_textview) TextView versionNameTextview;
    @Bind(R.id.progressbar) ProgressBar progressbar;

    private String serverVersionName;
    private int serverVersionCode;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);

//            LoadStuta loadStuta = null;
            switch (msg.what) {
                case LOAD_FINISHED:
                    if (serverVersionCode > getVersionCode()) {
                        buildDialog();
                    }
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "json 解析错误", Toast.LENGTH_SHORT).show(); // 提示
                    break;
                case URL_ERROR:
                    Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show(); // 提示
                    break;
                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "操作错误", Toast.LENGTH_SHORT).show(); // 提示
                    break;
            }
        }
    };
//
//    public enum LoadStuta {
//        LOAD_FINISHED,
//        JSON_ERROR,
//        URL_ERROR,
//        IO_ERROR
//    }


    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setCancelable(true).
                setTitle("有新版本").
                setMessage(desc).
                setPositiveButton("现在升级！", (whichDialog, which) ->
                {
                    Toast.makeText(this, "正在升级", Toast.LENGTH_SHORT).show(); // 提示
                }).
                setNegativeButton("等会儿通知我", ((dialog1, which) -> {
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show(); // 土司提示
//                    dialog
                })).show();
    }

    /**
     * 初始化所有的控件
     */
    public void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        versionNameTextview.setText(getVersionName());

        // 开启线程读取服务器端数据
        new Thread() {
            @Override
            public void run() {

                Message message = mHandler.obtainMessage();
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://192.168.31.101:8080/update/root/update.json").openConnection();
                    if (connection.getResponseCode() == 200) { // 返回回应码
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.connect();

                        InputStream inputStream = connection.getInputStream();
                        JSONObject jsonObject = new JSONObject(StreamUtils.input2String(inputStream));// 获取读取流

                        serverVersionName = jsonObject.getString("versionName"); // 服务器的版本号
                        serverVersionCode = jsonObject.getInt("versionCode");   // 服务器的版本名字
                        desc = jsonObject.getString("description");   // 服务器端描述

                        message.what = LOAD_FINISHED;
                        inputStream.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URL_ERROR;
                } catch (IOException e) {
                    message.what = IO_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 初始化事件
     */
    public void initEvent() {

    }

    /**
     * 获取本地版本名字
     *
     * @return 版本名字
     */
    public String getVersionName() {
        PackageManager manager = getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * 获取本地版本号码
     *
     * @return 版本号
     */
    public int getVersionCode() {
        PackageManager manager = getPackageManager();
        int versionCode = -1;
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }
}