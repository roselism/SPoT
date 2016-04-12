package com.roselism.spot.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.User;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/2/20.
 * <p/>
 * <p/>
 * 增
 * 闪
 * 改
 * 查
 */
public class FolderOperater extends Operater {
    private Folder mFolder;
//    private Context mContenxt;

//    public FolderOperater(Folder mFolder) {
//        this.mFolder = mFolder;
//    }

    /**
     * 操作某个文件夹
     *
     * @param mContenxt 上下文对象
     * @param mFolder   要被操作的文件夹对象（增删改查）
     */
    public FolderOperater(Context mContenxt, Folder mFolder) {
        this.mFolder = mFolder;
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
    }

    /**
     * 创建一个文件夹对象
     */
    public void createFolder() {
        final User creater = User.getCurrentUser(mContenxt, User.class);
//        final Folder folder = new Folder(name, creater);
        final onOperatListener listener = (onOperatListener) mContenxt; // 监听器
//        mFolder.setWorkers(null);

        mFolder.save(mContenxt, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContenxt, "文件夹创建成功", Toast.LENGTH_SHORT).show();
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