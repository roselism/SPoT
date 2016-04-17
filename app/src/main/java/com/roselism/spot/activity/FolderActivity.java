package com.roselism.spot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.melnykov.fab.FloatingActionButton;
import com.roselism.spot.R;
import com.roselism.spot.adapter.PictureGridAdapter;
import com.roselism.spot.model.dao.operator.PhotoOperater;
import com.roselism.spot.library.app.dialog.InviteFriendDialog;
import com.roselism.spot.library.app.dialog.SimpleInputDialog;
import com.roselism.spot.model.domain.File;
import com.roselism.spot.model.domain.bmob.Folder;
import com.roselism.spot.model.domain.bmob.Photo;
import com.roselism.spot.model.dao.operator.FolderOperater;
import com.roselism.spot.library.content.DataLoader;
import com.roselism.spot.util.LogUtils;
import com.roselism.spot.util.ThreadUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 文件夹详情Activity
 * 显示当前文件夹下的所有内容（当前内容为照片）
 */
public class FolderActivity extends AppCompatActivity
        implements View.OnTouchListener,
        View.OnClickListener,
        View.OnFocusChangeListener,
        SimpleInputDialog.OnInputFinishedListener {

    private static final String TAG = "FolderActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.picture_grid) GridView mGridView;
    @Bind(R.id.bg_image) ImageView mBgImage;
    @Bind(R.id.floatingButton) FloatingActionButton mFloatingButton;
    @Bind(R.id.overlay) DimOverlayFrameLayout overlay;
    @Bind(R.id.fab_sheet) CardView fabSheet;
    @Bind(R.id.add_friends_icon) ImageView mLinkIcon;
    @Bind(R.id.add_friends_text) TextView mLinkText;
    @Bind(R.id.add_friends_layout) RelativeLayout inviteLayout;
    @Bind(R.id.uplaod_icon) ImageView mUplaodIcon;
    @Bind(R.id.upload_text) TextView mUploadText;
    @Bind(R.id.upload_layout) RelativeLayout mUploadLayout;
    @Bind(R.id.download_icon) ImageView mDownloadIcon;
    @Bind(R.id.download_text) TextView mDownloadText;
    @Bind(R.id.download_layout) RelativeLayout mDownloadLayout;
    @Bind(R.id.share_icon) ImageView mShareIcon;
    @Bind(R.id.share_text) TextView mShareText;
    @Bind(R.id.share_layout) RelativeLayout mShareLayout;

    private List<File> mData; // 用于创建适配器的数据
    private Folder curFolder; // 目前所在的文件夹
    private MaterialSheetFab materialSheetFab; // fab 到 sheet的转换器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        ButterKnife.bind(this);

        // 获取当前文件夹的名字
//        Bundle bundle = this.getIntent().getExtras();
//        String curFolderId = bundle.getString("fileId");
        curFolder = new Folder(this.getIntent().getStringExtra("fileId"));
        String folderName = this.getIntent().getStringExtra("fileName");

        toolbar.setTitle(folderName); // 设置toolbar的Title为当前文件夹
        setSupportActionBar(toolbar); // 设置支持toolbar

        ThreadUtils.runInUIThread(new FolderLoader(curFolder, this)); // 开启一条线程，加载数据

        initClickListener(); // 初始化点击监听器

        // 初始化materialSheetFab 并使FloatingButton 与数据显示集相关联
        int sheetColor = getResources().getColor(R.color.white);
        int fabColor = getResources().getColor(R.color.white);
        materialSheetFab = new MaterialSheetFab(mFloatingButton, fabSheet, overlay, sheetColor, fabColor);
        materialSheetFab.showFab();
        mFloatingButton.attachToListView(mGridView);

        // 创建一个新用户，设置昵称为wangzhen，头像为null并且在创建完成时打印user的昵称
//        UserOperater.adder.newUser().
//                setNickName("wangzhen").
//                setProfile(null).
//                build(user -> LogUtils.d(user.getNickName()));
    }

    /**
     * 初始化所有点击监听器
     */
    private void initClickListener() {
        inviteLayout.setOnTouchListener(this);
        inviteLayout.setOnClickListener(this);
        mUploadLayout.setOnClickListener(this);
        mUploadLayout.setOnTouchListener(this);
        mDownloadLayout.setOnClickListener(this);
        mDownloadLayout.setOnTouchListener(this);
        mShareLayout.setOnClickListener(this);
        mShareLayout.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_floder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }

        return true;
    }

    /**
     * 建造适配器
     */
    protected void buildAdapter() {
//        Log.i(TAG, "buildAdapter: building...........");
        LogUtils.i("buildAdapter: building...........");
        PictureGridAdapter pictureGridAdapter = new PictureGridAdapter(this, mData);
        mGridView.setAdapter(pictureGridAdapter);
    }

    /**
     * 显示背景图片
     */
    private void showbgImage(boolean isShow) {
        if (isShow) { // 决定展示背景图片（默认情况下没有东西的时候就展示这张图片）

        } else { // 不展示

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.add_friends_layout:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(getResources().getColor(R.color.clouds));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackground(getDrawable(R.drawable.selector_sheet_button));
                    }
                }
                break;

            case R.id.upload_layout:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(getResources().getColor(R.color.clouds));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackground(getDrawable(R.drawable.selector_sheet_button));
                    }
                }
                break;

            case R.id.download_layout:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(getResources().getColor(R.color.clouds));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackground(getDrawable(R.drawable.selector_sheet_button));
                    }
                }
                break;

            case R.id.share_layout:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(getResources().getColor(R.color.belize_hole));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackground(getDrawable(R.drawable.selector_sheet_button_blue));
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friends_layout:
                SimpleInputDialog dialog = new InviteFriendDialog();
                dialog.show(getFragmentManager(), "invite people");
                break;

            case R.id.upload_layout:
                Intent intent = new Intent(this, ImagePickerActivity.class);
                intent.putExtra("folderId", curFolder.getObjectId());
                startActivity(intent);
                break;

            case R.id.download_layout:

                break;

            case R.id.share_layout:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.friendEmail_editText:
                if (hasFocus) {
                    materialSheetFab.hideSheet();
                } else {
                    materialSheetFab.showFab();
                }
                break;
        }
    }

    @Override
    public void onInputFinished(View view) {
        switch (view.getId()) {

            case R.id.friendEmail_editText:
                EditText editText = (EditText) view;
                String email = editText.getText().toString(); // 获取输入的邮箱
                FolderOperater operater = new FolderOperater(this, curFolder);
                operater.addWorker(email); // 给当前文件夹添加参与者

                break;
        }
    }

    /**
     * 查询相应的folder类
     */
    public class FolderLoader extends DataLoader {
        private String mFolderId;

        public FolderLoader(Folder folder, Context context) {
            super(context);
            this.mFolderId = folder.getObjectId();
        }

        @Override
        public void run() {
            if (mData != null)  // 在每次使用之前清空，避免刷新的时候数据重复
                mData.clear();
            else
                mData = new LinkedList<>();

//            PhotoOperater photoOperater = new PhotoOperater(outerClass);

            PhotoOperater.query.allPhotosFrom(mFolderId, (list) -> {
                for (Photo p : list) {
                    mData.add(new File(p));
                }
                ThreadUtils.runInUIThread(() -> buildAdapter());
            });

//
//            photoOperater.allPhotosFrom(mFolderId, (list) -> {
//                for (Photo p : (List<Photo>) list) {
//                    mData.add(new File(p));
//                }
//
//                ThreadUtils.runInUIThread(() -> buildAdapter());
//            });
        }
    }
}