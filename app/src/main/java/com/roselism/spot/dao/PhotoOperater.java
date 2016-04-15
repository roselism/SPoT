package com.roselism.spot.dao;

import android.content.Context;
import android.util.Log;

import com.roselism.spot.dao.listener.LoadFinishedListener;

import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.Photo;
import com.roselism.spot.domain.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Image对象的操作类
 * Created by hero2 on 2016/3/8.
 */
public class PhotoOperater extends Operater {

    private static final String TAG = "PhotoOperater";

    public PhotoOperater(Context context) {
        this.mContext = context;
    }


    /**
     * <<<<<<< HEAD
     * 获取相应文件夹下的所有的照片
     *
     * @param folderId photo所在的文件夹的id
     * @param listener 加载完毕监听器（如果失败则会返回null）
     */
    public void allPhotosFrom(String folderId, LoadFinishedListener listener) {
        BmobQuery<Photo> query = new BmobQuery<>();
        Folder folder = new Folder(folderId);
        query.addWhereEqualTo("parent", new BmobPointer(folder)); // 查询所有parent属性为folder的picture对象
        query.include("uploader");
        query.findObjects(mContext, new FindListener<Photo>() {
            @Override
            public void onSuccess(List<Photo> list) {
                listener.onLoadFinished(list);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: " + "错误码：" + i + " " + "错误信息：" + s + " !!!");
                listener.onLoadFinished(null);
            }
        });
    }

    /**
     * 主界面的所有照片
     */
    public void allPhotoInHome(BmobUser user, LoadFinishedListener listener) {

        // 查询picture
        BmobQuery<Photo> pictureQuery = new BmobQuery<>();
        pictureQuery.addWhereEqualTo("uploader", user);
        pictureQuery.addWhereDoesNotExists("parent"); // parent 列中没有值
        pictureQuery.include("uploader");
        pictureQuery.findObjects(mContext, new FindListener<Photo>() {
            @Override
            public void onSuccess(List<Photo> list) {

                listener.onLoadFinished(list);

//
//                for (Photo p : list)
//                    fileList.add(new com.roselism.spot.domain.File(p));
//
//                mData.addAll(fileList);
//                fileList.clear(); // 清除里面的所有数据，避免刷新时数据重复
//                    Log.i(TAG, "onSuccess: List<Photo> size" + list.size());

//                    finished();
            }

            @Override
            public void onError(int i, String s) {
//                    finished();
//                Log.i(TAG, "onError: Photo 查询失败--> " + "错误码：" + i + " 错误信息: " + s);
                listener.onLoadFinished(null);
            }
        });


    }


    /**
     * 获取相应文件夹下的所有的照片
     *
     * @param folder 文件夹对象
     */
    public void allPhotosFrom(Folder folder, LoadFinishedListener listener) {
        allPhotosFrom(folder.getObjectId(), listener);
    }

    /**
     * 上传所选中的照片
     * <p>
     * =======
     * 上传所选中的照片
     * <p>
     * >>>>>>> 032282a... init
     * 完整的过程：选中图片-> 上传图片并返回图片的url地址，储存Photo对象，并将返回的url对象赋值给Photo对象的相应属性
     *
     * @param parentFolderId 照片所在文件夹的Id，如果为根文件夹则应该传入 null
     * @deprecated 需要将所有图片都上传完毕之后才能上传与之对应的Photo对象
     */
    public void uploadFile(String parentFolderId, List<String> selectedFileUrls) {
        Log.i("TAG", "uploadFile: " + "-----------开始上传---------");

        final List<Photo> uploadingPhotos = new ArrayList<>(); // 将要持久化的Photo对象
//        String[] filePaths = list2Array(selectedFileUrls); // 将List<String> 转换到 String[]
        String[] filePaths = selectedFileUrls.toArray(new String[selectedFileUrls.size()]);
//        selectedFileUrls.toArray(new String[selectedFileUrls.size()]);

        uploadingPhotos.addAll(url2Photo(parentFolderId, filePaths));

        // 上传File
        Bmob.uploadBatch(mContext, filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> urls) {

                BmobFile curBmobFile; // 当前bmobFile对象
                String curUrl; // 当前url对象
                Photo curPhoto; // 当前pic对象

                Log.i(TAG, "onSuccess: current list size = " + list.size() + " urls size = " + urls.size());

                if (list.size() == uploadingPhotos.size()) { // 当list的size和uploadingPicture相同时，说明上传并返回完了url，这时可以开始update picture对象
                    List<Photo> tempPhotos = new LinkedList<>();
                    tempPhotos.addAll(uploadingPhotos); // 需要上传Photos的临时容器
                    int count = 0;
                    for (int i = 0; i < list.size(); i++) { // 遍历 上传成功的图片的列表
                        for (int j = 0; j < tempPhotos.size(); j++) { // 遍历将要上传的Picture对象
                            count++;
                            curBmobFile = list.get(i); // 遍历到的当前的文件
                            curUrl = urls.get(i); // 当前url
                            curPhoto = tempPhotos.get(j);
                            Log.i(TAG, "onSuccess: curBmobFile name = " + curBmobFile.getFilename());
                            if (curPhoto.getName().equals(curBmobFile.getFilename())) {
                                curPhoto.setPic(curUrl); // 获取图片的服务器地址
                                curPhoto.setPhoto(curBmobFile);

                                tempPhotos.remove(j);
//                                savePhoto(curPhoto);

                                Log.i(TAG, "onSuccess: tempPhotos size = " + tempPhotos.size() + " list size = " + list.size());
                                break;
                            }
                        }
                    }
                    Log.i(TAG, "onSuccess: count = " + count);

                    savePhoto(uploadingPhotos); // 开始上传Photo对象
                }
            }

            /**
             * @param curIndex curIndex--表示当前第几个文件正在上传
             * @param curPercent curPercent--表示当前上传文件的进度值（百分比）
             * @param total  total--表示总的上传文件数
             * @param totalPercent totalPercent--表示总的上传进度（百分比）
             */
            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
//                Log.i(TAG, "onProgress: current percent " + curPercent + "%");
//                Log.i(TAG, "onProgress: total percent " + totalPercent + "%");
//                progressDialog.setProgress(curIndex);
//                progressDialog.setSecondaryProgress(totalPercent);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: " + "错误码:" + i + " 错误信息:" + s);
            }
        });
    }

//    public String[] list2Array(List<String> list) {
//        String[] strings = new String[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            strings[i] = list.get(i);
//        }
//        return strings;
//    }

    /**
     * 将url变为Photo对象
     *
     * @param parentFolderId
     * @param filePaths
     * @return 返回从url转变成Photo的对象列表
     */
    private List<Photo> url2Photo(String parentFolderId, String[] filePaths) {
//        String curPath;
        Photo photo; // 要储存的Photo对象
        List<Photo> photoList = new LinkedList<>();
        for (String url : filePaths) {
            // 构造Picture对象
            File file = new File(url);
            Date takenDate = new Date(file.lastModified()); // 拍摄时间
            String fileName = url.substring(url.lastIndexOf("/") + 1);// 获取照片名
            User uploader = User.getCurrentUser(mContext, User.class); // 上传者

            photo = new Photo();
            photo.setName(fileName);
            photo.setUploader(uploader);
            photo.setTakenDate(new BmobDate(takenDate));
            photo.setParent(parentFolderId == null ? null : new Folder(parentFolderId));

            photoList.add(photo);
        }

        return photoList;
    }

    /**
     * 持久化Photo对象列表
     *
     * @param photos 要储存的Photo集
     */
    private void savePhoto(List<Photo> photos) {

        // 存储Picture对象
        for (Photo p : photos) {
            p.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.i("TAG", "onSuccess: " + " picture对象储存完成");
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
    }

    /**
     * 持久化单个photo对象
     *
     * @param photo 要被持久化的Photo对象
     */
    private void savePhoto(Photo photo) {
        photo.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}