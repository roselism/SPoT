package com.roselism.spot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.roselism.spot.adapter.PictureSelectAdapter;
import com.roselism.spot.model.db.dao.operator.PhotoOperater;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hero2 on 2016/3/6.
 */
public class UploadService extends Service {

    private static final String TAG = "UploadService";
    private List<String> photoUrlList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次启动的时候会调用
     * 如果服务已经启动 而再次点击启动按钮时 会去调用
     * <code>onStartCommand(Intent intent, int flags, int startId)</code> 方法
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "UploadService -->onCreate");
        photoUrlList = new LinkedList<>(); // 初始化
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "UploadService -->onStartCommand");
        photoUrlList.addAll(PictureSelectAdapter.mSelectedImage); //将新选择的url加入到上传列表
        PictureSelectAdapter.mSelectedImage.clear(); // 清空选中的图片url

        // 开始上传操作
        PhotoOperater photoOperater = new PhotoOperater(this); // 创建一个照片操作者
        photoOperater.uploadFile(intent.getStringExtra("folderId"), photoUrlList); // 上传选中的图片
        photoUrlList.clear(); // 清除自己的url

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "UploadService -->onDestroy");
        photoUrlList.clear();
        super.onDestroy();
    }
}