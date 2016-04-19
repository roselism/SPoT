package com.roselism.spot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roselism.spot.R;
import com.roselism.spot.SPoTApplication;
import com.roselism.spot.adapter.ListSwipeAdapter;
import com.roselism.spot.model.OnOperateListener;
import com.roselism.spot.model.StrategyContext;
import com.roselism.spot.model.dao.bmob.query.QueryFolderByCreater;
import com.roselism.spot.model.dao.bmob.query.QueryFolderByAssociateUser;
import com.roselism.spot.model.dao.operator.FolderOperater;
import com.roselism.spot.model.Operater;
import com.roselism.spot.model.dao.operator.PhotoOperater;
import com.roselism.spot.library.app.dialog.DetailProgressDialog;
import com.roselism.spot.library.app.dialog.FolderNameDialog;
import com.roselism.spot.model.domain.local.File;
import com.roselism.spot.model.domain.bmob.Folder;
import com.roselism.spot.model.domain.bmob.Photo;
import com.roselism.spot.model.domain.bmob.User;

import com.roselism.spot.util.LogUtil;
import com.roselism.spot.util.ThreadUtil;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * 主界面
 */
public class HomeActivity extends AppCompatActivity
        implements FolderNameDialog.FolderNameInputListener,
        View.OnClickListener, FolderOperater.onOperatListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.listview) ListView mListView;
    @Bind(R.id.bg_image) ImageView mBgImage;
    @Bind(R.id.nav_view) NavigationView mNavView;
    @Bind(R.id.drawer) DrawerLayout mDrawer;

    private User mCurUser = null; // 当前用户
    private List<File> mData; // 数据

    private DetailProgressDialog detailProgressDialog; // 数据上传进度表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bmob.initialize(this, "a736bff2e503810b1e7e68b248ff5a7d");

        mCurUser = BmobUser.getCurrentUser(this, User.class); // 当前用户

        if (mCurUser == null) { // 如果用户还未登录，则跳转至登陆界面
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        SPoTApplication.setUser(mCurUser);

        refreshData();

        // 初始化ImageLoader
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);

        detailProgressDialog = new DetailProgressDialog();

        initDrawer();
    }

    /**
     * 刷新界面数据
     */
    public void refreshData() {
        ThreadUtil.runInUIThread(new DataLoader());
    }

    /**
     * 初始化DrawerLayout
     */
    public void initDrawer() {
        // 给DrawerLayout绑定监听器
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavView.setNavigationItemSelectedListener(this);

        View headerView = mNavView.getHeaderView(0);
        ImageView profile = (ImageView) headerView.findViewById(R.id.profile);
        TextView name = (TextView) headerView.findViewById(R.id.name);
        TextView emailAddress = (TextView) headerView.findViewById(R.id.email_address);

        mCurUser.setNickName("王镇");
        name.setText(mCurUser.getNickName());
        emailAddress.setText(mCurUser.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add: // 添加一个相册
                showInputDialog();
                break;

            case R.id.item_upload:// 上传照片
                startActivity(new Intent(this, ImagePickerActivity.class));
                break;

            case R.id.item_progress: // 展示当前上传进度
                DetailProgressDialog dialog = new DetailProgressDialog();
                dialog.show(getFragmentManager(), "progress");
                break;

            case R.id.item_exist: // 退出登录
                BmobUser.logOut(this); // 注销登陆
                startActivity(new Intent(this, LoginActivity.class)); // 跳转到登陆界面
                finish();
                break;
        }
        return true;
    }

    /**
     * 展示输入文件夹的对话框
     */
    void showInputDialog() {
        // 创建一个对话框，里面得到新创建的文件夹的名字
        FolderNameDialog folderNameDialog = new FolderNameDialog();
        folderNameDialog.show(getFragmentManager(), "create folder");
    }

    /**
     * 为数据建造适配器
     */
    public void buildAdapter() {
//        Log.i(TAG, "buildAdapter: running");
        ListSwipeAdapter listSwipeAdapter = new ListSwipeAdapter(mData, this);
        mListView.setAdapter(listSwipeAdapter);
        initListItemListener();
    }

    /**
     * 初始化listview的item点击事件的监听器
     */
    public void initListItemListener() {

        // 为每个list item设置监听器
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File curFile = mData.get(position); // 被电击的file

                // 制醋要id
                if (curFile.getType() == File.GALLARY_TYPE) // 相册类型
                    startActivity(new Intent(HomeActivity.this, FolderActivity.class)
                            .putExtra("fileId", curFile.getId())
                            .putExtra("fileName", curFile.getTitle()));

                if (curFile.getType() == File.PICTURE_TYPE) { // 图片类型
                    // TODO: 2016/4/13 跳转进浏览picture专用的Activity中，而不是Folder中，这里还需要更改
                    startActivity(new Intent(HomeActivity.this, FolderActivity.class).
                            putExtra("fileId", curFile.getId()).putExtra("fileName", curFile.getTitle()));
                }
            }
        };

        mListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onInputFinished(String name) {
        FolderOperater folderOperater = new FolderOperater(this, new Folder(name, mCurUser));
        folderOperater.createFolder();//创建该文件夹
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void showBackGround(boolean show) {
        if (show) mBgImage.setVisibility(View.VISIBLE);
        else mBgImage.setVisibility(View.GONE);
    }

    @Override
    public void onOperateCreate(Folder folder, User creater, int state) {
        if (state == com.roselism.spot.model.dao.operator.Operater.CREATE_SUCCESS) {
            refreshData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_friends:
                startActivity(new Intent(this, ContactsActivity.class));
                break;
        }

        return false;
    }

    protected <T extends BmobUser> T getUser() {
        return (T) User.getCurrentUser(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(mDrawer))

            super.onBackPressed();
    }

    /**
     * 读取存放在首页的所有的照片和相册的任务
     */
    private class DataLoader implements Runnable, OnOperateListener<List<File>> {

        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;

        @Override
        public void run() {
            if (mData == null) {
                mData = new ArrayList<>(); // 初始化，每次使用的时候初始化，避免数据重复
            } else
                mData.clear();

//            BmobUser curUser = getUser();

            // 设定operater的操作
            Operater<List<Folder>> bmobOperater = (StrategyContext strategyPackage, OnOperateListener<List<Folder>> listener) -> {
                strategyPackage.Do(listener);
            };
            bmobOperater.operate( // 设定operater的行为
                    new StrategyContext(new QueryFolderByAssociateUser(getUser())),
                    (list -> {
                        if (list != null)
                            for (Folder f : list) {
                                mData.add(new File(f, 1));
                            }
                        else
                            LogUtil.i("用户关联相册为null");

                        flag1 = true;
                        onOperated(null);
                    }));
            bmobOperater.operate(
                    new StrategyContext(new QueryFolderByCreater(getUser())),
                    (list -> {
                        if (list != null) {
                            LogUtil.i("数据不为bull");
                            for (Folder f : list) {
                                mData.add(new File(f, 0));
                            }
                        } else
                            LogUtil.i("用户创建相册为null");
                        Log.i(TAG, "run: mdata.size -->" + mData.size());
                        flag2 = true;
                        onOperated(null);// 数据已经添加了进去,所以这里赋值为空就行
                    }));

//            FolderOperater operater = new FolderOperater(null);
//            operater.findFolderAssoiateWith(curUser, (data) -> { // 获取与用户相关联的文件夹
//                if (data != null) {
//                    LogUtil.i("数据不为null");
//
//                    for (Folder f : (List<Folder>) data) {
//                        mData.add(new File(f, 1));
//                    }
//                } else
//                    LogUtil.i("用户关联相册为null");
//
//                flag1 = true;
//                onLoadFinished(null);
//            });


//            operater.findFolderCreateBy(curUser, (data) -> {
//                if (data != null) {
//                    LogUtil.i("数据不为bull");
//                    for (Folder f : (List<Folder>) data) {
//                        mData.add(new File(f, 0));
//                    }
//                } else
//                    LogUtil.i("用户创建相册为null");
//                Log.i(TAG, "run: mdata.size -->" + mData.size());
//                flag2 = true;
//                onOperated(null);// 数据已经添加了进去,所以这里赋值为空就行
//            });


            PhotoOperater.query.allPhotoInHome(getUser(), (data) -> {
                if (data != null)
                    for (Photo p : data)
                        mData.add(new File(p));

                flag3 = true;
                onOperated(null);
            });

        }

        /**
         * 所有数据加载完毕时调用
         *
         * @param data 这里传入null就好，数据在传入之前就已经添加进了mdata成员变量里面
         */
//        @Override
//        public void onLoadFinished(List<File> data) {
//            if (flag1 && flag2 && flag3) { // 如果三个都加载完毕，则执行加载数据
//                ThreadUtil.runInUIThread(() -> {
//                    LogUtil.i("mdata size = " + mData.size());
//                    Collections.sort(mData, (s1, s2) -> {// 给加载到的数据进行排序->文件夹在上，图片在下
//                        if (s1.getType() != s2.getType())
//                            return -(s1.getType() - s2.getType()); // 图片类型为0, 相册类型为1 相册大于图片，但是要排在上面，所以要取负
//                        return 1;
//                    });
//
//                    // 设置背景
//                    if (mData.size() == 0)
//                        showBackGround(true);
//                    else
//                        showBackGround(false);
//
//                    buildAdapter();
//                });
//            }
//        }


        /**
         * 所有数据加载完毕时调用
         *
         * @param files 这里传入null就好，数据在传入之前就已经添加进了mdata成员变量里面
         */
        @Override
        public void onOperated(List<File> files) {
            if (flag1 && flag2 && flag3) { // 如果三个都加载完毕，则执行加载数据
                ThreadUtil.runInUIThread(() -> {
                    LogUtil.i("mdata size = " + mData.size());
                    Collections.sort(mData, (s1, s2) -> {// 给加载到的数据进行排序->文件夹在上，图片在下
                        if (s1.getType() != s2.getType())
                            return -(s1.getType() - s2.getType()); // 图片类型为0, 相册类型为1 相册大于图片，但是要排在上面，所以要取负
                        return 1;
                    });

                    // 设置背景
                    if (mData.size() == 0)
                        showBackGround(true);
                    else
                        showBackGround(false);

                    buildAdapter();
                });
            }
        }
    }
}