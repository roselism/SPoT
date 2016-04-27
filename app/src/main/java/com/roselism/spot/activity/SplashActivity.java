package com.roselism.spot.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roselism.spot.R;
import com.roselism.spot.util.HttpConnectionHelper;
import com.roselism.spot.util.StreamUtils;
import com.roselism.spot.util.ThreadUtil;
import com.roselism.spot.util.convert.Converter;
import com.roselism.spot.util.convert.InStream2OutStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";

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

    @Override
    protected void onDestroy() {
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
                    Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show(); // 提示
                    download();// 开启子线程，进行下载
                }).
                setNegativeButton("一会儿再说", ((dialog1, which) -> {
                    dialog1.dismiss();
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
     * 服务器的版本信息与本地信息进行对比，查看是否能够升级
     */
    public void checkUpdate() {
        if (serverVersionCode > getVersionCode()) { //有新版本
//            download();
            showUpdateDialog(); // 提示一个窗口，询问用户是否升级
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
                    // 在ui线程中执行
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

    /**
     * 跳转到主界面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * 下载新的app 安装包
     */
    /**
     * 下载apk
     */
    public void download() {
        Log.i(TAG, "download: 开始下载");

        new Thread() {
            @Override
            public void run() {
//                super.run();
                HttpConnectionHelper helper = new HttpConnectionHelper.Builder().
                        setRequestMethod(HttpConnectionHelper.POST_METHOD).
                        setConnectionTimeOut(5000).
                        setReadTimeOut(5000).
                        setPath(downloadUrl).
                        build();

                Log.i(TAG, "run: url = " + downloadUrl);
                InputStream inputStream = null;
                OutputStream output = null;
                try {
                    if (helper.isResponseOk()) {
                        Log.i(TAG, "run: -->" + "连接正常");
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //sd 卡挂在
                            File updateApk = new File(getFilesDir(), "SPoT-" + serverVersionName + ".apk"); //创建一个文件

                            // 输入转输出
                            inputStream = helper.getConnection().getInputStream();
                            Converter<InputStream, OutputStream> converter = new InStream2OutStream(updateApk);
                            output = converter.convert(inputStream); // 获取转换数据

                            Log.i(TAG, "run: -->" + "下载完毕");

                            output.flush();// 刷新
                            helper.getConnection().disconnect();

                            // 跳转到系统下载页面
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setDataAndType(Uri.fromFile(updateApk), "application/vnd.android.package-archive");
                            startActivityForResult(intent, 0);// 如果用户取消安装的话,会返回结果,回调方法onActivityResult
                            // startActivity(intent);

                        } else {
                            Toast.makeText(SplashActivity.this, "未发现sd卡", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "run: 未发现sd卡, environment state= " + Environment.getExternalStorageState());
                        }
                    } else {
                        Log.i(TAG, "run: 响应码：" + helper.responseCode());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: io 错误");
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (inputStream != null)
                            inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


}