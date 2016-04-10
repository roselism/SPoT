package com.roselism.spot.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.roselism.spot.domain.RelationLink;
import com.roselism.spot.domain.User;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/3/14.
 */
public class RelationLinkOperater extends Operater {

    private static final String TAG = "RelationLinkOperater";

    private Context mContext; // 上下文对象

    public RelationLinkOperater(Context mContext) {
        this.mContext = mContext;
    }

    public void addFriends(User user, User friend) {
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

        BmobQuery<RelationLink> linkQuery = new BmobQuery<>();
        linkQuery.addWhereEqualTo("user", new BmobPointer(user));
        linkQuery.findObjects(mContext, new FindListener<RelationLink>() {
            @Override
            public void onSuccess(List<RelationLink> list) {
                Log.i(TAG, "onSuccess: 查找Link成功");

                RelationLink link;
                if (list.size() == 0) { // 没有 创建
                    link = new RelationLink();
                    link.setUser(user);
                    link.addAllUnique("friendsId", friendsId); // 添加数组
                    link.save(mContext, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "onSuccess: 添加成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i(TAG, "onError: 添加失败:" + i + " 错误信息: " + s);
                        }
                    }); // 储存
                } else if (list.size() == 1) { // 有 修改
                    link = list.get(0);
                    link.addAllUnique("friendsId", friendsId); // 添加数组
                    link.update(mContext, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(mContext, "添加好友成功", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onSuccess: 添加好友成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.i(TAG, "onError: 错误码:" + i + " 错误信息: " + s);

                        }
                    }); // 添加
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 查找link失败 错误码:" + i + " 错误信息: " + s);

                RelationLink link = new RelationLink();
                link.save(mContext); // 初始化RelationLink表
                link.delete(mContext);
            }
        });
    }
}