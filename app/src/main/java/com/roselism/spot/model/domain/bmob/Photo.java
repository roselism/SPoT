package com.roselism.spot.model.domain.bmob;

import android.content.Context;
import android.util.Log;

import com.roselism.spot.model.domain.local.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;

/**
 * 持久层 Photo 对象
 * 用户拍摄的照片
 * 有别于 image和picture
 * <p>
 * Created by hero2 on 2016/1/26.
 * 请不要做任何修改！
 */
public class Photo extends BmobObject {
    private String name; // 照片的名字
    private Folder parent;// 在哪个文件夹下，默认为null（根目录）
    private String size; // 照片的大小
    private String picUrl; // 照片的服务器地址
    private BmobDate takenDate; // 照片的拍摄时间
    private User uploader; // 上传者
    private BmobFile photoFile; // 与picture对相关联的照片

    public Photo() {
    }

    public Photo(File file) {
        setObjectId(file.getId()); // 只需获得pic对象的id
        setName(file.getTitle());
        setPic(file.getFileUrl());
        setPhotoFile(file.getBoundFile());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPic() {
        return picUrl;
    }

    public void setPic(String picUrl) {
        this.picUrl = picUrl;
    }

    public BmobDate getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(BmobDate takenDate) {
        this.takenDate = takenDate;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public BmobFile getPhoto() {
        return photoFile;
    }


    public BmobFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(BmobFile photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    public void delete(final Context context, DeleteListener listener) {
        super.delete(context, listener); // 删除图片对象
        // 删除和图片对象关联的pic
        BmobFile file = new BmobFile();
        file.setUrl(photoFile.getUrl());
        file.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                Log.i("TAG", "onSuccess: Picture关联的pic删除成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("TAG", "onFailure: Picture关联的pic删除失败！！！" + " 错误码：" + i + " " + "错误信息" + s);
            }
        });
    }
}