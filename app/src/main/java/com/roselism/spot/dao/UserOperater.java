package com.roselism.spot.dao;

import android.content.Context;

import com.roselism.spot.library.app.SAM.SetContext;
import com.roselism.spot.dao.listener.BuildFinishedListener;
import com.roselism.spot.dao.listener.LoadFinishedListener;
import com.roselism.spot.domain.Image;
import com.roselism.spot.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 操作User model的方法类
 * Created by simon on 2016/4/13.
 * 增
 * 删
 * 改
 * 查
 */
public class UserOperater {
    public static AddOperater adder;
    public static QueryOperater query;

    public Context mContext;

//    public User setContext(Context context) {
//        mContext = context;
//    }

    /**
     *
     */
    public class QueryOperater extends AddOperater implements SetContext<QueryOperater> {

        @Override
        public QueryOperater setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public void getUserByEmail(String email, LoadFinishedListener<User> listener) {
            if (mContext == null)
                throw new IllegalArgumentException("上下文对象不能为null,需要在此方法之前调用setContext(Context context) 方法");
            BmobQuery<User> query = new BmobQuery<>(); // 查询
            query.addWhereEqualTo("email", email);
            query.findObjects(mContext, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {

                    listener.onLoadFinished(list);
//                    User friends = list.get(0);
//                    User currentUser = BmobUser.getCurrentUser(mContext, User.class);
//
//                    RelationLinkOperater operater = new RelationLinkOperater(mContext);
//                    operater.addFriend(currentUser, friends); // 添加好友
                }

                @Override
                public void onError(int i, String s) {
//                    Log.i(TAG, "onError: User查询错误 错误码:" + i + " 错误信息: " + s);
                    listener.onLoadFinished(null);
                }
            });
        }
    }


    /**
     * 增加器
     * 用于创建新的对象
     */
    public class AddOperater extends UserOperater {
        volatile User user;

        public AddOperater setContext(Context context) {
            mContext = context;
            return this;
        }

        public AddOperater newUser() {
            return this;
        }

        public AddOperater setNickName(String nickName) {
            user.setNickName(nickName);
            return this;
        }

        public AddOperater setProfile(Image profile) {
//            this.profile = profile;
            user.setProfile(profile);
            return this;
        }

        /**
         * 建造对象
         */
        public void build(BuildFinishedListener<User> listener) {
            if (mContext == null || user.getProfile() == null)
                throw new IllegalArgumentException("参数错误");

            user.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    listener.onBuildFinished(user);
                }

                @Override
                public void onFailure(int i, String s) {
                    listener.onBuildFinished(null);
                }
            });
        }
    }
}