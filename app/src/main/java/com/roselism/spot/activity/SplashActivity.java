package com.roselism.spot.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roselism.spot.R;
import com.roselism.spot.util.StreamUtils;
import com.roselism.spot.util.ThreadUtil;

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
//    private static final int LOAD_FINISHED = 0;
//    private static final int JSON_ERROR = 1;
//    private static final int URL_ERROR = 2;
//    private static final int IO_ERROR = 3;

    @Bind(R.id.versionName_textview) TextView versionNameTextview;
    @Bind(R.id.progressbar) ProgressBar progressbar;

    private String serverVersionName;
    private int serverVersionCode;
    private String desc;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected void onDestroy() {
//        mHandler = null;
        super.onDestroy();
    }

    /**
     * 显示升级对话框
     */
    public void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setCancelable(true).
                setTitle("新版本:" + serverVersionName).
                setMessage(desc).
                setPositiveButton("现在升级！", (whichDialog, which) ->
                {
                    Toast.makeText(this, "正在升级", Toast.LENGTH_SHORT).show(); // 提示
                }).
                setNegativeButton("一会儿再说", ((dialog1, which) -> {
                    dialog1.dismiss();

//                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show(); // 土司提示
                })).show();
    }

    /**
     * 跳入主界面
     */
    void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化所有的控件
     */
    public void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        versionNameTextview.setText(getVersionName());
        readVersionInfo();
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
     * 服务器的版本信息与本地信息进行对比，查看是否能够升级
     */
    public void checkUpdate() {
        if (serverVersionCode > getVersionCode()) { //有新版本

        }
    }

    /**
     * 从服务器端获取app的版本信息
     */
    public void readVersionInfo() {
        // 开启线程读取服务器端数据
        new Thread() {
            @Override
            public void run() {

//                Message message = mHandler.obtainMessage();
                Runnable runnable = null;
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
                        downloadUrl = jsonObject.getString("downloadUrl"); // 下载地址

//                        message.what = LOAD_FINISHED;
                        inputStream.close();
//                        ThreadUtil.runInUIThread(() -> checkUpdate()); // 在ui线程中执行
                        runnable = () -> checkUpdate(); // 检查更新
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
//                    message.what = URL_ERROR;
//                    ThreadUtil.runInUIThread(() -> Toast.makeText(SplashActivity.this, "saf", Toast.LENGTH_SHORT).show());
                    ; // 在ui线程中执行
                    runnable = () -> Toast.makeText(SplashActivity.this, "URL地址不正确", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
//                    message.what = IO_ERROR;
//                    ThreadUtil.runInUIThread(() -> Toast.makeText(SplashActivity.this, "saf", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                    runnable = () -> Toast.makeText(SplashActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
//                    message.what = JSON_ERROR;
//                    ThreadUtil.runInUIThread(() -> Toast.makeText(SplashActivity.this, "saf", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                    runnable = () -> Toast.makeText(SplashActivity.this, "JSON格式解析错误", Toast.LENGTH_SHORT).show();
                } finally {
//                    mHandler.sendMessage(message);
                    ThreadUtil.runInUIThread(runnable); // 启动
                }
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
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