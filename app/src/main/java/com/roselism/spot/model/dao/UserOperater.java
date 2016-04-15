package com.roselism.spot.model.dao;

import android.content.Context;

import com.roselism.spot.model.domain.Image;
import com.roselism.spot.model.domain.User;
import com.roselism.spot.model.dao.listener.BuildFinishedListener;

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
    public static addOperater adder;
    public Context mContext;


    /**
     * 增加器
     * 用于创建新的对象
     */
    public class addOperater extends UserOperater {
        volatile User user;

        public addOperater setContext(Context context) {
            mContext = context;
            return this;
        }

        public addOperater newUser() {
            return this;
        }

        public addOperater setNickName(String nickName) {
            user.setNickName(nickName);
            return this;
        }

        public addOperater setProfile(Image profile) {
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