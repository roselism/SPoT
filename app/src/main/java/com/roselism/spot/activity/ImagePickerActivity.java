package com.roselism.spot.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roselism.spot.R;
import com.roselism.spot.adapter.PictureSelectAdapter;
import com.roselism.spot.model.domain.local.ImageFolder;
import com.roselism.spot.service.UploadService;
import com.roselism.spot.library.app.ListImageDirPopupWindow;
import com.roselism.spot.util.ThreadUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片选择Activity，用于选择图片，并且获取选择图片的url
 */
public class ImagePickerActivity extends AppCompatActivity
        implements ListImageDirPopupWindow.OnImageDirSelected, View.OnClickListener {

    private static final String TAG = "ImagePickerActivity";

    @Bind(R.id.gridview) GridView mGridView;
    @Bind(R.id.gallary_name_text) TextView mFolderName;
    @Bind(R.id.image_count_text) TextView mImageCount;
    @Bind(R.id.bottom_layout) RelativeLayout mBottomLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private int mPicsSize; // 存储文件夹中的图片数量
    private File mDefaultImgDir; // 图片数量最多的文件夹
    private List<String> mAllImgList; // 所有的图片

    private PictureSelectAdapter mAdapter; // 适配器
    //    public static final List<Photo> uploadingPhoto = new LinkedList<>(); // 暂时存储没有被赋值pic属性的Picture对象
    private HashSet<String> mDirPaths = new HashSet<>(); // 临时的辅助类，用于防止同一个文件夹的多次扫描
    private List<ImageFolder> mImgFloderList = new ArrayList<>(); // 扫描拿到所有的图片文件夹

    int totalCount = 0;
    private int mScreenHeight;
    private ProgressDialog mProgressDialog;
    private ListImageDirPopupWindow mListImageDirPopupWindow; // 展示图片文件夹的popupWindow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        getImages(); // 获取照片
        initClickListener(); // 初始化点击监听器
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mDefaultImgDir == null) {
            Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到", Toast.LENGTH_SHORT).show();
            return;
        }

        // 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAllImgList = Arrays.asList(mDefaultImgDir.list());
        mAdapter = new PictureSelectAdapter(getApplicationContext(),
                mAllImgList,
                R.layout.grid_item_select_pic,
                mDefaultImgDir.getAbsolutePath()); // 创建适配器
        mGridView.setAdapter(mAdapter); // 给gridview设置适配器
        mImageCount.setText(totalCount + "张");  // 底部栏
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_check: // 上传
                //                uploadSelectedImage();
                String folderId = ImagePickerActivity.this.getIntent().getStringExtra("folderId") == null ?
                        null : ImagePickerActivity.this.getIntent().getStringExtra("folderId"); // 获取folderId

                Intent intent = new Intent(ImagePickerActivity.this, UploadService.class);
                intent.putExtra("folderId", folderId);
                startService(intent); // 启动服务
//                PictureSelectAdapter.mSelectedImage.clear();
                startActivity(new Intent(ImagePickerActivity.this, HomeActivity.class));
                Toast.makeText(this, "开始上传", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                WindowManager.LayoutParams.MATCH_PARENT,
                (int) (mScreenHeight * 0.7),
                mImgFloderList,
                LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ImagePickerActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Log.e("TAG", mCursor.getCount() + "");
                while (mCursor.moveToNext()) {

                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));// 获取图片的路径
                    Log.e("TAG", path);

                    if (firstImage == null) firstImage = path; // 拿到第一张图片的路径
                    File parentFile = new File(path).getParentFile();// 获取该图片的父路径名
                    if (parentFile == null) continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFolder imageFolder = null;

                    if (mDirPaths.contains(dirPath)) // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        continue;
                    else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFolder = new ImageFolder();
                        imageFolder.setDir(dirPath);
                        imageFolder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;

                    imageFolder.setCount(picSize);
                    mImgFloderList.add(imageFolder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mDefaultImgDir = parentFile;
                    }
                }
                mCursor.close();
                mDirPaths = null;// 扫描完成，辅助的HashSet也就可以释放内存了

                ThreadUtil.runInUIThread(() -> {
                    mProgressDialog.dismiss(); // 取消dialog
                    data2View();  //  为View绑定数据
                    initListDirPopupWindw();  // 初始化展示文件夹的popupWindw
                });
//                mHandler.sendEmptyMessage(0x110); // 通知Handler扫描图片完成

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_layout:  //为底部的布局设置点击事件，弹出popupWindow
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mListImageDirPopupWindow.showAsDropDown(mBottomLayout, 0, 0);

                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHandler = null;
    }

    /**
     * 初始化所有点击监听器
     */
    private void initClickListener() {
        mBottomLayout.setOnClickListener(this);
    }

    @Override
    public void selected(ImageFolder floder) {

        mDefaultImgDir = new File(floder.getDir());
        mAllImgList = Arrays.asList(mDefaultImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));

        //可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
        mAdapter = new PictureSelectAdapter(getApplicationContext(), mAllImgList,
                R.layout.grid_item_select_pic, mDefaultImgDir.getAbsolutePath());
        mGridView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        mFolderName.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }
}