package com.roselism.spot.model.dao.data.bmob.query;

import com.roselism.spot.model.dao.OnOperateListener;
import com.roselism.spot.model.dao.Strategy;
import com.roselism.spot.model.dao.operator.RelationLinkOperater;
import com.roselism.spot.model.domain.bmob.User;
import com.roselism.spot.util.ThreadUtil;

import java.util.List;

/**
 * 查询一个用户的所有好友
 * Created by simon on 16-4-19.
 */
public class QueryFriendsByUser implements Strategy {

    User user;

    public QueryFriendsByUser(User user) {
        this.user = user;
    }

    @Override
    public void Do(OnOperateListener onOperateListener) {
        RelationLinkOperater.query.friendsListOf(user, (friends) -> {
            onOperateListener.onOperated(friends);
        });
    }
}