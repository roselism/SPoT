package com.roselism.spot.model.dao.bmob.query;

import com.roselism.spot.model.OnOperateListener;
import com.roselism.spot.model.Strategy;
import com.roselism.spot.model.dao.operator.FolderOperater;
import com.roselism.spot.model.domain.bmob.Folder;
import com.roselism.spot.model.domain.bmob.User;
import com.roselism.spot.util.LogUtil;

import java.util.List;

/**
 * 查找某用户被邀请参与的文件夹
 * Created by simon on 16-4-19.
 */
public class QueryFolderByAssociateUser implements Strategy<OnOperateListener<List<Folder>>> {
    User user;

    public QueryFolderByAssociateUser(User user) {
        this.user = user;
    }

    @Override
    public void Do(OnOperateListener<List<Folder>> listener) {
        FolderOperater operater = new FolderOperater(null);
        operater.findFolderAssoiateWith(user, (data) -> { // 获取与用户相关联的文件夹
            if (data != null) {
                LogUtil.i("数据不为null");
                listener.onOperated(data);
            } else {
                listener.onOperated(null);
                LogUtil.i("用户关联相册为null");
            }
        });
    }
}