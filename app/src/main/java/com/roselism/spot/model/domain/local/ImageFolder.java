package com.roselism.spot.model.domain.local;

/**
 * Created by hero2 on 2016/1/24.
 * <p/>
 * 存放有图片的文件夹
 * 请不要做任何修改！
 */
public class ImageFolder {
    private String dir; // 该文件夹的路径
    private String firstImagePath; // 第一张图片的路径
    private String name; // 文件夹的名称
    private int count; // 该文件夹下的图片的总数

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = dir.lastIndexOf("/");
        this.name = dir.substring(lastIndexOf); // 设置文件夹名称
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
