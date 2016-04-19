package com.roselism.spot.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gordonwong.materialsheetfab.DimOverlayFrameLayout;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.roselism.spot.R;
import com.roselism.spot.adapter.ContactsAdapter;
import com.roselism.spot.library.app.UserListener;
import com.roselism.spot.model.Operater;
import com.roselism.spot.model.StrategyContext;
import com.roselism.spot.model.dao.bmob.query.QueryFriendsByUser;
import com.roselism.spot.model.dao.operator.RelationLinkOperater;
import com.roselism.spot.library.widget.decorator.DividerItemDecoration;
import com.roselism.spot.library.app.dialog.InviteFriendDialog;
import com.roselism.spot.library.app.dialog.SimpleInputDialog;
import com.roselism.spot.model.dao.operator.UserOperater;
import com.roselism.spot.model.domain.bmob.User;

import com.roselism.spot.library.widget.MenuActionButton;
import com.roselism.spot.library.widget.RecyclerViewScrollListener;
import com.roselism.spot.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 联系人Activity，用于显示当前用户的所有好友
 */
public class ContactsActivity extends AppCompatActivity
        implements View.OnClickListener, SimpleInputDialog.OnInputFinishedListener, View.OnFocusChangeListener, UserListener<User> {

    public static final String TAG = "ContactsActivity";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recylerview) RecyclerView mRecylerview;
    @Bind(R.id.floatingButton) MenuActionButton mFloatingButton;
    @Bind(R.id.overlay) DimOverlayFrameLayout overlay;
    @Bind(R.id.add_friends_icon) ImageView addFriendsIcon;
    @Bind(R.id.add_friends_text) TextView addFriendsText;
    @Bind(R.id.add_friends_layout) RelativeLayout addFriendsLayout;
    @Bind(R.id.uplaod_icon) ImageView uplaodIcon;
    @Bind(R.id.upload_text) TextView uploadText;
    @Bind(R.id.upload_layout) RelativeLayout uploadLayout;
    @Bind(R.id.download_icon) ImageView downloadIcon;
    @Bind(R.id.download_text) TextView downloadText;
    @Bind(R.id.download_layout) RelativeLayout downloadLayout;
    @Bind(R.id.share_icon) ImageView shareIcon;
    @Bind(R.id.share_text) TextView shareText;
    @Bind(R.id.share_layout) RelativeLayout shareLayout;
    @Bind(R.id.fab_sheet) CardView fabSheet;

    private MaterialSheetFab materialSheetFab; // fab 到 sheet的转换器
    private List<User> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initListener();
        initMaterialSheetFab();

        ThreadUtil.runInThread(new DataLoader(getUser(), this));

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

    public void buildAdapter() {
        ContactsAdapter adapter = new ContactsAdapter(mData, this);
        mRecylerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friends_layout:
                InviteFriendDialog dialog = new InviteFriendDialog();
                dialog.show(this.getFragmentManager(), "dialog");

                break;
        }
    }

    @Override
    public void onInputFinished(View view) {

        EditText editText = (EditText) view;
        String friendsEdmail = editText.getText().toString();

        UserOperater.query.getUserByEmail(friendsEdmail, (data) -> {
            if (data != null || data.size() >= 1) {
                RelationLinkOperater operater = new RelationLinkOperater(this);
//                RelationLinkOperater
//                operater.addFriend(getUser(), data.get(0));
                RelationLinkOperater.adder.addFriend(getUser(), data.get(0));
//          UserOperater
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

    @Override
    public User getUser() {
        return User.getCurrentUser(this, User.class);
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

//            RelationLinkOperater operater = new RelationLinkOperater(SPoTApplication.getContext());
//            operater.friendsListOf(getUser(), (friends) -> {
//                for (User user : (List<User>) friends)
//                    mData.add(user);
//
//                ThreadUtil.runInUIThread(() -> buildAdapter()); // 加载完毕，调用buildAdapter
////                onLoadFinished();
//            });

            Operater<List<User>> bmobOperater = (strategyContext, listener) -> {
                strategyContext.Do(listener);
            };
            bmobOperater.operate(
                    new StrategyContext(new QueryFriendsByUser(getUser())),
                    (users) -> {
                        for (User user : users)
                            mData.add(user);

                        ThreadUtil.runInUIThread(() -> buildAdapter()); // 加载完毕，调用buildAdapter
                    }
            );
//            RelationLinkOperater.query.friendsListOf(getUser(), (friends) -> {
//                for (User user : (List<User>) friends)
//                    mData.add(user);
//
//                ThreadUtil.runInUIThread(() -> buildAdapter()); // 加载完毕，调用buildAdapter
////                onLoadFinished();
//            });
        }
    }
}