package com.roselism.spot.model.domain;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Photo 和 Folder 的抽象
 * <p/>
 * Created by hero2 on 2016/1/26.
 */
public class File {
    public static final int PICTURE_TYPE = 0;
    public static final int GALLARY_TYPE = 1;
    public static final int VIDEO_TYPE = 2;

    private String id; // photo和Folder的id
    private String title; // photo 是 照片名称， 相册是相册名
    private String subtitle; // photo 是照片的大小， 相册是里面包含的内容总数
    private String date; // photo 是拍摄时间，相册是创建时间
    private String fileUrl; // 图片url
    private User user; // photo是上传人，相册是创建人
    private Integer type; // 文件类型
    private BmobFile boundFile; // 与file对象关联的文件

    public File(Photo photo) {
        id = photo.getObjectId();
        type = PICTURE_TYPE;
        title = photo.getName();
        subtitle = photo.getSize();
        date = photo.getTakenDate().getDate().toString();
        fileUrl = photo.getPic();
        user = photo.getUploader();
        boundFile = photo.getPhoto();
//        Log.i("tag", toString());
    }

    public File(Folder folder, int count) {
        id = folder.getObjectId();
        type = GALLARY_TYPE;
        title = folder.getName();
        subtitle = count + "";     //  当前floder下的照片的数量
        date = folder.getCreatedAt(); // 2016-02-01 19:59:51
        fileUrl = null; //在适配器中会进行适配，这里赋值为null就可以
        user = folder.getCreater();
//        Log.i("TAG", toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobFile getBoundFile() {
        return boundFile;
    }

    public void setBoundFile(BmobFile boundFile) {
        this.boundFile = boundFile;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String info = "id = " + id + "\n" +
                "title = " + title + "\n" +
                "subtitle = " + subtitle + "\n" +
                "date = " + date + "\n" +
                "file url = " + fileUrl + "\n" +
                "user id = " + user.getObjectId() + "\n" +
                "type = " + type + "\n";
//                "bound file = " + boundFile.getFilename();

        return info;
    }
}