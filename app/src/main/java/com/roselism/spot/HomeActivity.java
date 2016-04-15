package com.roselism.spot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.roselism.spot.adapter.ListSwipeAdapter;
import com.roselism.spot.adapter.PictureListAdapter;
import com.roselism.spot.dao.FolderOperater;
import com.roselism.spot.dao.Operater;
import com.roselism.spot.library.app.dialog.DetailProgressDialog;
import com.roselism.spot.library.app.dialog.FolderNameDialog;
import com.roselism.spot.domain.File;
import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.Photo;
import com.roselism.spot.domain.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.roselism.spot.domain.File.GALLARY_TYPE;

/**
 * 主界面
 */
public class HomeActivity extends AppCompatActivity
        implements FolderNameDialog.FolderNameInputListener,
        PictureListAdapter.OnItemClickListener,
        View.OnClickListener, FolderOperater.onOperatListener,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.listview) ListView mListView;
    @Bind(R.id.bg_image) ImageView mBgImage;
    @Bind(R.id.nav_view) NavigationView mNavView;
    @Bind(R.id.drawer) DrawerLayout mDrawer;

    private List<File> mData;
    Thread mDataThread;
    DetailProgressDialog detailProgressDialog;
    User mCurUser;

    public final static int SELECT_MOD = 99; // 选择模式
    public final static int NORMAL_MOD = 97; // 正常模式，点击进入详情
    public final static int ENTER_MOD = 96; // 为照片选择父文件夹模式

    public final static String FILE_LOAD_KEY = "loadKey";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

//            Log.i(TAG, "handleMessage: handle message");

            switch (msg.what) {
                case DataLoader.LOAD_FINISHED:
                    if (mData.size() == 0) showBackGround(true);
                    else showBackGround(false);

                    buildAdapter();
                    break;
            }

            Bundle bundle = msg.getData();
            if (bundle.getString("folderId") != null) {
                buildConfirmDialog(bundle.getString("folderId"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bmob.initialize(this, "a736bff2e503810b1e7e68b248ff5a7d");
        mCurUser = User.getCurrentUser(this, User.class);

        if (mCurUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        if (mDataThread == null) {
            mDataThread = new Thread(new DataLoader());
            mDataThread.start();
        }

        // 初始化ImageLoader
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
        initClickListener();

        detailProgressDialog = new DetailProgressDialog();

        initDrawer();
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
     * 创建一个文件夹对象
     */
    private void createFolder(String name) {
        Folder folder = new Folder(name, User.getCurrentUser(this, User.class));
        folder.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(HomeActivity.this, "文件夹创建成功", Toast.LENGTH_SHORT).show();
                mDataThread.start(); //创建成功之后需要重新刷新数据
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i(TAG, "onFailure: " + i + " " + s);
            }
        });
    }

    /**
     * 为数据建造适配器
     */
    public void buildAdapter() {
        Log.i(TAG, "buildAdapter: running");
        ListSwipeAdapter listSwipeAdapter = new ListSwipeAdapter(mData, this);
        mListView.setAdapter(listSwipeAdapter);
        switchListListener(NORMAL_MOD);
    }

    /**
     * 切换listview的item监听器
     *
     * @param model
     */
    public void switchListListener(final int model) {
        Log.i(TAG, "switchListListener: " + model);

        AdapterView.OnItemClickListener itemClickListener;
        switch (model) {
            case HomeActivity.NORMAL_MOD: // 正常模式，单击进入详情
                itemClickListener = new AdapterView.OnItemClickListener() { // 为每个list item设置监听器
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File curFile = mData.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", curFile.getType() == File.GALLARY_TYPE ? File.PICTURE_TYPE + "" : GALLARY_TYPE + "");
                        bundle.putString("fileId", curFile.getId());
                        bundle.putString("fileName", curFile.getTitle());

                        Intent intent = new Intent(HomeActivity.this, FolderActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                };
                break;

            case HomeActivity.SELECT_MOD: // 选择模式, 点击list item时也会触发ImageButton的onClick方法 drop
                itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                };
                break;

            case ENTER_MOD: // drop
                itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File parentFolder = mData.get(position);
                        Message m = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("folderId", parentFolder.getId());
                        m.setData(bundle);
                        mHandler.sendMessage(m);
                    }
                };
                break;
            default: //默认为正常状态 drop
                itemClickListener = new AdapterView.OnItemClickListener() { // 为每个list item设置监听器
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File curFile = mData.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("fileType", curFile.getType() == File.GALLARY_TYPE ? File.PICTURE_TYPE + "" : GALLARY_TYPE + "");
                        bundle.putString("fileId", curFile.getId());
                        bundle.putString("fileName", curFile.getTitle());
                        Intent intent = new Intent(HomeActivity.this, FolderActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                };
                break;
        }
        mListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onInputFinished(String name) {
        FolderOperater folderOperater = new FolderOperater(this, new Folder(name, mCurUser));
        folderOperater.createFolder();//创建该文件夹
//        folderOperater.createFolder();

//        createFolder(name); // 创建文件夹
    }

    @Override
    public void onFirstItemClick(View view) {
        Log.i(TAG, "------------ onFirstItemClick: ------------");
        switchListListener(SELECT_MOD);
//        mOperateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoItemSelected() {
        Log.i(TAG, "------------ onNoItemSelected: ------------");
        switchListListener(NORMAL_MOD);
    }

    void initClickListener() {
    }

    /**
     * 创建一个确定对话框
     *
     * @param folderId
     */
    protected void buildConfirmDialog(final String folderId) {

        // 创建一个对话框，提醒是否移动到该文件夹
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("移动到当前文件夹");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                // 更新数据
                Photo pic;
                for (File f : PictureListAdapter.mSelectedItem) {
                    pic = new Photo(f);
                    pic.setParent(new Folder(folderId));
                    pic.update(HomeActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(HomeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                            PictureListAdapter.mSelectedItem.clear(); // 清空选中项
//                            reader.run(); // 刷新一遍数据
                            mDataThread.start();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(HomeActivity.this, i + " " + s, Toast.LENGTH_SHORT).show();
                            PictureListAdapter.mSelectedItem.clear(); // 清空选中项
//                            reader.run(); // 刷新一遍列表
                            mDataThread.start();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PictureListAdapter.removeAll();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
        if (state == Operater.CREATE_SUCCESS) {
//            reader.run(); // 如果创建成功 则重新装载数据
            mDataThread.start();
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

    /**
     * 读取存放在首页的所有的照片和相册的任务
     */
    private class DataLoader implements Runnable {

        public static final int LOAD_FINISHED = 0x16;

        @Override
        public void run() {
            mData = new ArrayList<>(); // 初始化，每次使用的时候初始化，避免数据重复

            final List<File> fileList = new ArrayList<>(); // 临时列表，用于将Picture和Folder对象转换成File对象
            BmobUser curUser = User.getCurrentUser(HomeActivity.this);

            // 查询floder
            BmobQuery<Folder> query1 = new BmobQuery<>(); // 第一个条件，查询出自己创建的
            query1.addWhereEqualTo("creater", new BmobPointer(curUser));
            BmobQuery<Folder> query2 = new BmobQuery<>(); // 第二个条件，查询出被邀请的
            query2.addWhereContains("workers", curUser.getObjectId());

            List<BmobQuery<Folder>> queryList = new ArrayList<>(); // 联合查询
            queryList.add(query1);
            queryList.add(query2);

            BmobQuery<Folder> mainQuery = new BmobQuery<>(); // 主查询语句
            mainQuery.or(queryList);
            mainQuery.include("creater"); // 查询文件夹的创建人
            mainQuery.findObjects(HomeActivity.this, new FindListener<Folder>() {
                @Override
                public void onSuccess(List<Folder> list) {
                    for (Folder f : list)
                        fileList.add(new File(f, 0));// 先将数量设置为0，一会儿设置专门的进程来查询
                    mData.addAll(fileList);
                    fileList.clear(); // 清空fileList
                    Log.i(TAG, "onSuccess: Folder查询成功" + list.size());
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "onError: Folder 查询失败--> " + "错误码：" + i + " 错误信息: " + s);
                }
            });

            // 查询picture
            BmobQuery<Photo> pictureQuery = new BmobQuery<>();
            pictureQuery.addWhereEqualTo("uploader", curUser);
            pictureQuery.addWhereDoesNotExists("parent"); // parent 列中没有值
            pictureQuery.include("uploader");
            pictureQuery.findObjects(HomeActivity.this, new FindListener<Photo>() {
                @Override
                public void onSuccess(List<Photo> list) {
                    for (Photo p : list)
                        fileList.add(new File(p));

                    mData.addAll(fileList);
                    fileList.clear(); // 清除里面的所有数据，避免刷新时数据重复
//                    Log.i(TAG, "onSuccess: List<Photo> size" + list.size());

                    finished();
                }

                @Override
                public void onError(int i, String s) {
                    finished();
                    Log.i(TAG, "onError: Photo 查询失败--> " + "错误码：" + i + " 错误信息: " + s);
                }
            });
        }

        public void finished() {
            mHandler.sendEmptyMessage(LOAD_FINISHED); // 向handler发送消息，通知已经file装填完了
        }
    }
}