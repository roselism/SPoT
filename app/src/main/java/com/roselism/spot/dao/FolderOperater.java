package com.roselism.spot.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

<<<<<<< HEAD
import com.roselism.spot.dao.listener.LoadFinishedListener;
import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
=======
import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.User;

import cn.bmob.v3.datatype.BmobRelation;
>>>>>>> 032282a... init
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/2/20.
<<<<<<< HEAD
 * <p>
 * <p>
=======
 * <p/>
 * <p/>
>>>>>>> 032282a... init
 * 增
 * 闪
 * 改
 * 查
 */
public class FolderOperater extends Operater {
    private Folder mFolder;
<<<<<<< HEAD
=======
//    private Context mContenxt;

//    public FolderOperater(Folder mFolder) {
//        this.mFolder = mFolder;
//    }
>>>>>>> 032282a... init

    /**
     * 操作某个文件夹
     *
     * @param mContenxt 上下文对象
     * @param mFolder   要被操作的文件夹对象（增删改查）
     */
    public FolderOperater(Context mContenxt, Folder mFolder) {
        this.mFolder = mFolder;
<<<<<<< HEAD
        this.mContext = mContenxt;
    }

    /**
     * 查找某个用户创建的所有文件夹
     *
     * @param user
     */
    public void findFolderCreateBy(BmobUser user, LoadFinishedListener listener) {
        BmobQuery<Folder> query1 = new BmobQuery<>(); // 第一个条件，查询出自己创建的
        query1.addWhereEqualTo("creater", new BmobPointer(user));
        query1.include("creater"); // 查询文件夹的创建人
        query1.findObjects(mContext, new FindListener<Folder>() {
            @Override
            public void onSuccess(List<Folder> list) {
                listener.onLoadFinished(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onLoadFinished(null);
            }
        });
    }

//    public void

    /**
     * 查找某用户被邀请参与的文件夹
     *
     * @param user 目标用户
     */
    public void findFolderAssoiateWith(BmobUser user, LoadFinishedListener listener) {
        BmobQuery<Folder> query2 = new BmobQuery<>(); // 第二个条件，查询出被邀请的
        query2.addWhereContains("workers", user.getObjectId());
        query2.findObjects(mContext, new FindListener<Folder>() {
            @Override
            public void onSuccess(List<Folder> list) {
                listener.onLoadFinished(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onLoadFinished(null);
            }
        });
    }

    /**
     * 查找与某个文件夹所有相关联的user（比如当前用户被邀请加入文件夹）
     * // TODO: 2016/4/13  l
     */


    /**
     * 给某个文件夹下添加参与者（只有读和上传权限，以及删除自己上传的照片的权限）
     *
     * @param email 参与者的email
     * @return
     */
    public void addWorker(String email) {

        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("email", email);
        query.findObjects(mContext, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                BmobRelation relation = new BmobRelation();
                relation.add(list.get(0));
                mFolder.setWorkers(relation);
                mFolder.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.i("TAG", "onSuccess: ");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i("TAG", "onFailure: " + i + " " + s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }
        });
=======
        this.mContenxt = mContenxt;
    }

    /**
     * 查找被该用户创建的文件夹
     *
     * @param user
     */
    public void findFolderCreateBy(User user) {

    }

    public void findFolderAssoiateWith(User user) {

    }

    /**
     * 添加workers
     *
     * @param user 要被添加的用户
     * @return
     */
    public boolean addWorker(User user) {
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        mFolder.setWorkers(relation);
        mFolder.update(mContenxt, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i("TAG", "onSuccess: ");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("TAG", "onFailure: " + i + " " + s);
            }
        });

        return true;
>>>>>>> 032282a... init
    }

    /**
     * 创建一个文件夹对象
     */
    public void createFolder() {
<<<<<<< HEAD
        final User creater = User.getCurrentUser(mContext, User.class);
//        final Folder folder = new Folder(name, creater);
        final onOperatListener listener = (onOperatListener) mContext; // 监听器
//        mFolder.setWorkers(null);

        mFolder.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "文件夹创建成功", Toast.LENGTH_SHORT).show();
=======
        final User creater = User.getCurrentUser(mContenxt, User.class);
//        final Folder folder = new Folder(name, creater);
        final onOperatListener listener = (onOperatListener) mContenxt; // 监听器
//        mFolder.setWorkers(null);

        mFolder.save(mContenxt, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContenxt, "文件夹创建成功", Toast.LENGTH_SHORT).show();
>>>>>>> 032282a... init
                //创建成功之后需要重新刷新数据
//                reader.run(); // 在回掉函数中执行
                listener.onOperateCreate(mFolder, creater, CREATE_SUCCESS);
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("TAG", "onFailure: " + i + " " + s);
                listener.onOperateCreate(mFolder, creater, CREATE_FALLS);
            }
        });
    }
}