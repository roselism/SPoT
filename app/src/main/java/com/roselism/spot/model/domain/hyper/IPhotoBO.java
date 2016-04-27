package com.roselism.spot.model.domain.hyper;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by simon on 2016/4/21.
 */
public class IPhotoBO {
    private String name; // 照片的名字
    private IFolderBO parent;// 在哪个文件夹下，默认为null（根目录）
    private String size; // 照片的大小
    private String picUrl; // 照片的服务器地址
    private BmobDate takenDate; // 照片的拍摄时间
    private IUserBO uploader; // 上传者
    private BmobFile photoFile; // 与picture相关联的照片
}