package com.roselism.spot.model.dao.operator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.roselism.spot.model.dao.listener.OnLoadListener;
import com.roselism.spot.model.domain.bmob.RelationLink;
import com.roselism.spot.model.domain.bmob.User;
import com.roselism.spot.util.LogUtils;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/3/14.
 */
public class RelationLinkOperater extends Operater {

    private static final String TAG = "RelationLinkOperater";

    /**
     * 构建一个关系链对象
     *
     * @param context 上下文对象
     */
    public RelationLinkOperater(@NonNull Context context) {
        this.mContext = context;
    }

    public void addFriend(User user, User friend) {

        String id = friend.getObjectId();
        List<String> idList = new LinkedList<>();
        idList.addAll(Arrays.asList(id));
        addFriends(user, idList);
    }

    /**
     * 添加好友
     *
     * @param user      要添加好友的user
     * @param friendsId 好友的id
     */
    public void addFriends(final User user, final List<String> friendsId) {

        allRelationLinkOf(user, (data -> {
            LogUtils.i(TAG, "onSuccess: 查找Link成功");

            if (data != null) {
                RelationLink link;
                if (data.size() == 0) { // 没有 创建

                    link = new RelationLink();
                    link.setUser(user);
                    link.addAllUnique("friendsId", friendsId); // 添加数组
                    link.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i(TAG, "onSuccess: 添加成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            LogUtils.i(TAG, "onError: 添加失败:" + i + " 错误信息: " + s);
                        }
                    }); // 储存
                } else if (data.size() == 1) { // 有 修改
                    link = data.get(0);

                    link.addAllUnique("friendsId", friendsId); // 添加数组
                    link.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(mContext, "添加好友成功", Toast.LENGTH_SHORT).show();
                            LogUtils.i(TAG, "onSuccess: 添加好友成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            LogUtils.i(TAG, "onError: 错误码:" + i + " 错误信息: " + s);
                        }
                    }); // 添加
                }
            }
        }));
    }

    /**
     * 查询某个用户的所有的关系链
     *
     * @param user     需要查询的用户
     * @param listener 加载监听器
     */
    public void allRelationLinkOf(User user, OnLoadListener<RelationLink> listener) {

        BmobQuery<RelationLink> query = new BmobQuery<>();
        query.addWhereEqualTo("user", new BmobPointer(user)); // 查询当前用户的关系链
        query.findObjects(this.mContext, new FindListener<RelationLink>() {
            @Override
            public void onSuccess(List<RelationLink> list) {
                listener.onLoadFinished(list);
                LogUtils.i(TAG, "onSuccess: --> allRelationLinkOf");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.i(TAG, "onError: --> allRelationLinkOf");
//                LogUtils.i(TAG, "onError: " + "i = " + i + " s = " + s);
                listener.onLoadFinished(null);
            }
        });
    }

    /**
     * 查询某个用户的所有的好友
     *
     * @param user     将要加载他的所有好友
     * @param listener 数据加载监听器
     */
    public void friendsListOf(User user, OnLoadListener listener) {

        List<User> friends = new ArrayList<>();

        allRelationLinkOf(user, (relationLinks) -> {
            if (relationLinks != null && relationLinks.size() >= 1) {
                final RelationLink link = relationLinks.get(0);
                for (String id : link.getFriendsId()) {

                    BmobQuery<User> query1 = new BmobQuery<>();
                    query1.getObject(mContext, id, new GetListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            friends.add(user);
                            if (link.getFriendsId().size() == friends.size())
                                listener.onLoadFinished(friends);
                            LogUtils.i(TAG, "onSuccess: 查询用户成功 用户邮箱为:" + user.getEmail());
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            LogUtils.i(TAG, "查询用户失败: " + " 错误码:" + i + " 错误信息:" + s);
                            listener.onLoadFinished(null);
                        }
                    });
                }
            }
        });
    }
}