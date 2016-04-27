package com.roselism.spot.model.engine.bmob.query;

import com.roselism.spot.SPoTApplication;
import com.roselism.spot.model.db.dao.listener.OnLoadListener;
import com.roselism.spot.model.db.dao.stragegy.QueryStrategy;
import com.roselism.spot.model.domain.bmob.RelationLink;
import com.roselism.spot.model.domain.bmob.User;
import com.roselism.spot.util.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 查询一个用户所有的好友链
 * Created by simon on 16-4-18.
 */
public class QueryRelationLinkByOwner implements QueryStrategy<RelationLink> {

    User user;

    /**
     * 查询一个user的所有好友链
     *
     * @param user 需要查询的好友
     */
    public QueryRelationLinkByOwner(User user) {
        this.user = user;
    }

    @Override
    public void query(OnLoadListener<RelationLink> listener) {
        BmobQuery<RelationLink> query = new BmobQuery<>();
        query.addWhereEqualTo("user", new BmobPointer(user)); // 查询当前用户的关系链
        query.findObjects(SPoTApplication.getContext(), new FindListener<RelationLink>() {
            @Override
            public void onSuccess(List<RelationLink> list) {
                listener.onLoadFinished(list);
                LogUtil.i("onSuccess: --> allRelationLinkOf");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.i("onError: --> allRelationLinkOf");
                listener.onLoadFinished(null);
            }
        });
    }
}
