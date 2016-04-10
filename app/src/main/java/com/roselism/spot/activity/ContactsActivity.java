package com.roselism.spot.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.roselism.spot.R;
import com.roselism.spot.adapter.ContactsAdapter;
import com.roselism.spot.dao.RelationLinkOperater;
import com.roselism.spot.library.widget.decorator.DividerItemDecoration;
import com.roselism.spot.library.app.dialog.InviteFriendDialog;
import com.roselism.spot.library.app.dialog.SimpleInputDialog;
import com.roselism.spot.domain.RelationLink;
import com.roselism.spot.domain.User;
import com.roselism.spot.library.widget.MenuActionButton;
import com.roselism.spot.library.widget.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

/**
 * 联系人Activity，用于显示当前用户的所有好友
 */
public class ContactsActivity extends AppCompatActivity
        implements View.OnClickListener, SimpleInputDialog.OnInputFinishedListener,
        View.OnFocusChangeListener {

    public static final String TAG = "ContactsActivity";

    @Bind(R.id.toolbar)
    Toolbar               toolbar;
    @Bind(R.id.recylerview)
    RecyclerView          mRecylerview;
    @Bind(R.id.floatingButton)
    MenuActionButton      mFloatingButton;
    @Bind(R.id.overlay)
    DimOverlayFrameLayout overlay;
    @Bind(R.id.add_friends_icon)
    ImageView             addFriendsIcon;
    @Bind(R.id.add_friends_text)
    TextView              addFriendsText;
    @Bind(R.id.add_friends_layout)
    RelativeLayout        addFriendsLayout;
    @Bind(R.id.uplaod_icon)
    ImageView             uplaodIcon;
    @Bind(R.id.upload_text)
    TextView              uploadText;
    @Bind(R.id.upload_layout)
    RelativeLayout        uploadLayout;
    @Bind(R.id.download_icon)
    ImageView             downloadIcon;
    @Bind(R.id.download_text)
    TextView              downloadText;
    @Bind(R.id.download_layout)
    RelativeLayout        downloadLayout;
    @Bind(R.id.share_icon)
    ImageView             shareIcon;
    @Bind(R.id.share_text)
    TextView              shareText;
    @Bind(R.id.share_layout)
    RelativeLayout        shareLayout;
    @Bind(R.id.fab_sheet)
    CardView              fabSheet;

    private MaterialSheetFab materialSheetFab; // fab 到 sheet的转换器
    private Thread           dataThread; // 数据线程
    private List<User>       mData;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case DataLoader.LOAD_FINISHED:
                    buildAdapter();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initListener();
        initMaterialSheetFab();

        if (dataThread == null) {
            dataThread = new Thread(new DataLoader(User.getCurrentUser(this, User.class), this));
            dataThread.start();
        }

        mRecylerview.setAdapter(new ContactsAdapter(null, this));
        mRecylerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));
        mRecylerview.setOnScrollListener(new RecyclerViewScrollListener(4) {
            @Override
            public void onScrollUp() {
                mFloatingButton.show(); // 显示floatingbutton
                materialSheetFab.showFab();
            }

            @Override
            public void onScrollDown() {
                mFloatingButton.hide();
                //                materialSheetFab.
            }
        });
    }

    void initListener() {
        addFriendsLayout.setOnClickListener(this);
    }

    /**
     * 初始化materialSheetFab
     */
    void initMaterialSheetFab() {
        int sheetColor = getResources().getColor(R.color.white);
        int fabColor = getResources().getColor(R.color.white);
        materialSheetFab = new MaterialSheetFab(mFloatingButton, fabSheet, overlay, sheetColor, fabColor);
        materialSheetFab.showFab();
    }

    //    void initRecylerview() {
    //        mRecylerview.setAdapter(build());
    //    }

    public void buildAdapter() {
        Log.i(TAG, "buildAdapter: ------");
        ContactsAdapter adapter = new ContactsAdapter(mData, this);
        mRecylerview.setAdapter(adapter);
//        return adapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friends_layout:
                InviteFriendDialog dialog = new InviteFriendDialog();
                dialog.show(this.getFragmentManager(), "dialog");
//                dialog.show();

                break;
        }
    }

    @Override
    public void onInputFinished(View view) {
//        Log.i(TAG, "onInputFinished: -->");

        EditText editText = (EditText) view;
        String friendsEdmail = editText.getText().toString();
//        Log.i(TAG, "onInputFinished: email --> " + friendsEdmail);

        BmobQuery<User> query = new BmobQuery<>(); // 查询
        query.addWhereEqualTo("email", friendsEdmail);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
//                Log.i(TAG, "onSuccess: 查询成功");

                User friends = list.get(0);
                User currentUser = BmobUser.getCurrentUser(ContactsActivity.this, User.class);

                RelationLinkOperater operater = new RelationLinkOperater(ContactsActivity.this);
                operater.addFriends(currentUser, friends); // 添加好友
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: User查询错误 错误码:" + i + " 错误信息: " + s);
            }
        });
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


    /**
     * 数据加载器
     */
    public class DataLoader extends com.roselism.spot.library.content.DataLoader {
        private User user; // 需要被展示其好友列表的用户

        public DataLoader(User who, Context outerClass) {
            super(outerClass);
            this.user = who;
        }

        @Override
        public void run() {
            if (mData != null)  // 在每次使用之前清空，避免刷新的时候数据重复
                mData.clear();
            else
                mData = new ArrayList<>();

            BmobQuery<RelationLink> query = new BmobQuery<>();
            query.addWhereEqualTo("user", new BmobPointer(user));
            query.findObjects(outerClass, new FindListener<RelationLink>() {
                @Override
                public void onSuccess(List<RelationLink> list) {
                    Log.i(TAG, "onSuccess: 查询link成功");
                    Log.i(TAG, "onSuccess: linkList size = " + list.size());

                    if (list.size() >= 1) {
                        final RelationLink link = list.get(0);
                        for (String id : link.getFriendsId()) {
                            BmobQuery<User> query1 = new BmobQuery<>();
                            query1.getObject(outerClass, id, new GetListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    Log.i(TAG, "onSuccess: 查询用户成功 用户邮箱为:" + user.getEmail());
                                    mData.add(user);

                                    if (link.getFriendsId().size() == mData.size())
                                        onLoadFinished();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Log.i(TAG, "查询用户成功: " + " 错误码:" + i + " 错误信息:" + s);
                                    onLoadFinished();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "onFailure: " + " 错误码:" + i + " 错误信息:" + s);
                    onLoadFinished();
                }

                /**
                 * 当加载完毕
                 */
                public void onLoadFinished() {
//                    Log.i(TAG, "onLoadFinished: 数据加载完毕");
//                    Log.i(TAG, "onLoadFinished: mData size = " + mData.size());
                    mHandler.sendEmptyMessage(LOAD_FINISHED); // 通知handler数据加载完毕了
                }
            });
        }

        @Override
        public void onLoadFinished() {
            mHandler.sendEmptyMessage(LOAD_FINISHED);
        }
    }
}