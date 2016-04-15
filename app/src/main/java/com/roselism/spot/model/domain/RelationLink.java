package com.roselism.spot.model.domain;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * <p>关系链</p>
 * <p>用来表示用户和用户间的好友关系</p>
 * <p>每个用户最多只能拥有一个关系链</p>
 * 链子两端的任意用户的操作都会对整条链子造成影响，如一个用户删除和别人的链接（如解除好友关系）
 * <p>
 * <p>
 * Created by hero2 on 2016/3/14.
 */
public class RelationLink extends BmobObject {
//    private static String TAG = "RelationLink";

    private User user;
    private List<String> friendsId;

    public RelationLink() {
    }

    /**
     * 要添加关系的两人
     *
     * @param user      该用户
     * @param friendsId 需要被添加的好友的Id
     */
    public RelationLink(User user, List<String> friendsId) {
        this.user = user;
        this.friendsId = friendsId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(List friendsId) {
        this.friendsId = friendsId;
    }
}