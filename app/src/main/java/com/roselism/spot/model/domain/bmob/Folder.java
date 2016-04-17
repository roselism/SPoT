package com.roselism.spot.model.domain.bmob;

import android.content.Context;

import com.roselism.spot.SPoTApplication;
import com.roselism.spot.model.dao.operator.PhotoOperater;
import com.roselism.spot.model.domain.File;
import com.roselism.spot.util.LogUtils;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;

/**
 * 持久层 Folder 对象
 * 照片等文件的文件夹
 * <p>
 * Created by hero2 on 2016/1/26.
 * <p>
 * 继承BmobObject，是为了能让其他类能够指向它
 * 请不要做任何修改！
 */
public class Folder extends BmobObject {
    private String name;// 文件夹的名称 (不可变对象)
    private User creater; // 文件夹的创建者
    private BmobRelation workers;// 被邀请的用户

    public Folder(String name, User creater) {
        this.name = name;
        this.creater = creater;
    }

    public Folder(File file) {
        setObjectId(file.getId());
        setName(file.getTitle());
        setCreater(file.getUser());
    }

    /**
     * 查询时使用，请不要使用该构造函数构造出来的对象的save等方法
     *
     * @param objectId
     */
    public Folder(String objectId) {
        setObjectId(objectId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobRelation getWorkers() {
        return workers;
    }

    public void setWorkers(BmobRelation workers) {
        this.workers = workers;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    @Override
    public void delete(final Context context, DeleteListener listener) {
        super.delete(context, listener); // 删除当前的Folder对象
//        BmobQuery<Photo> query = new BmobQuery<>();
        PhotoOperater.query.allPhotosFrom(this, (list -> { // 查询当前文件夹下的所有的照片
            for (Photo p : list) { // 遍历当前文件夹下的图片
                if (p.getUploader().getObjectId().equals(SPoTApplication.getUser().getObjectId())) // 只删除自己的照片
                    PhotoOperater.deleter.delete(p, (photo) -> {
                        if (photo != null) {
                            LogUtils.i("照片" + photo.getName() + "删除成功");
                        }
                    });

//                    p.delete(context, new DeleteListener() {
//                        @Override
//                        public void onSuccess() {
//                            LogUtils.i("TAG", "onSuccess:" + Folder.this.getName() + "文件夹下的 图片删除成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//
//                        }
//                    });
            }
        }));


//        query.addWhereEqualTo("parent", new BmobPointer(this));
//
//        query.findObjects(context, new FindListener<Photo>() {
//            @Override
//            public void onSuccess(List<Photo> list) {
//
//
//                for (Photo p : list) { // 遍历当前文件夹下的图片
//                    p.delete(context, new DeleteListener() {
//                        @Override
//                        public void onSuccess() {
//                            LogUtils.i("TAG", "onSuccess:" + Folder.this.getName() + "文件夹下的 图片删除成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//
//                        }
//                    });
//                }
//
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
    }
}