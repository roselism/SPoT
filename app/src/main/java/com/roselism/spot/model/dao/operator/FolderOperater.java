package com.roselism.spot.model.dao.operator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.roselism.spot.model.dao.listener.OnLoadListener;
import com.roselism.spot.model.dao.listener.OnUpdateListener;
import com.roselism.spot.model.domain.bmob.Folder;
import com.roselism.spot.model.domain.bmob.User;
import com.roselism.spot.util.LogUtil;


import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/2/20.
 * <p>
 * 增
 * 闪
 * 改
 * 查
 *
 * @version 1.0
 * @deprecated
 */
public class FolderOperater extends Operater {
    private Folder mFolder;
    public static AddOperater adder = getAdder();
    public static UpdateOperater updater = getUpdater();

    private static AddOperater getAdder() {
        return new AddOperater(null);
    }

    private static UpdateOperater getUpdater() {
        return new UpdateOperater(null);
    }

    /**
     * 操作某个文件夹
     *
     * @param mContenxt 上下文对象
     * @param mFolder   要被操作的文件夹对象（增删改查）
     * @deprecated 不再推荐使用 请使用 FolderOperater(Folder folder) 作为替代
     */
    public FolderOperater(Context mContenxt, Folder mFolder) {
        this.mFolder = mFolder;
        this.mContext = mContenxt;
    }

    /**
     * @param folder 要被操作的文件夹    void test() {
     *               QueryOperater queryOperater = new BmobOperater();
     *               queryOperater.query(new QueryStrategyPackage(new UserQuery()), new OnLoadListener() {
     * @Override public void onLoadFinished(List data) {
     * <p>
     * }
     * });
     * }
     */
    public FolderOperater(Folder folder) {
        this.mFolder = folder;
    }

    /**
     * 查找某个用户创建的所有文件夹
     *
     * @param user
     */
    public void findFolderCreateBy(BmobUser user, OnLoadListener listener) {
        BmobQuery<Folder> query1 = new BmobQuery<>(); // 第一个条件，查询出自己创建的
        query1.addWhereEqualTo("creater", new BmobPointer(user));
        query1.include("creater"); // 查询文件夹的创建人
        query1.findObjects(mContext, new FindListener<Folder>() {
            @Override
            public void onSuccess(List<Folder> list) {
                listener.onLoadFinished(list);
                LogUtil.i("findFolderCreateBy: list size= " + list.size());
                LogUtil.i("findFolderCreateBy", "onSuccess: ");

            }

            @Override
            public void onError(int i, String s) {
//                LogUtil.i("TAG", "onFailure: " + i + " " + s);
                LogUtil.i("onFailure: " + i + " " + s);
                LogUtil.i("FolderOperater", "onError: -->");
                LogUtil.i("FolderOperater", "onError: --> is debug?" + LogUtil.isDebug());
                listener.onLoadFinished(null);
            }
        });
    }

    /**
     * 查找某用户被邀请参与的文件夹
     *
     * @param user 目标用户
     */
    public void findFolderAssoiateWith(BmobUser user, OnLoadListener listener) {
        BmobQuery<Folder> query2 = new BmobQuery<>(); // 第二个条件，查询出被邀请的
        query2.addWhereContains("workers", user.getObjectId());
        query2.findObjects(mContext, new FindListener<Folder>() {
            @Override
            public void onSuccess(List<Folder> list) {
                listener.onLoadFinished(list);
                LogUtil.i("findFolderAssoiateWith: 查询成功");
            }

            @Override
            public void onError(int i, String s) {
                listener.onLoadFinished(null);
                LogUtil.i("onFailure: " + i + " " + s);
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
                LogUtil.i("list size= " + list.size());

                BmobRelation relation = new BmobRelation();
                relation.add(list.get(0));
                mFolder.setWorkers(relation);
                mFolder.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        LogUtil.i("TAG", "onSuccess: ");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogUtil.i("TAG", "onFailure: " + i + " " + s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.i("onFailure: " + i + " " + s);
            }
        });
    }

    /**
     * 创建一个文件夹对象
     */
    public void createFolder() {
        final User creater = User.getCurrentUser(mContext, User.class);
        final onOperatListener listener = (onOperatListener) mContext; // 监听器

        mFolder.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "文件夹创建成功", Toast.LENGTH_SHORT).show();

                //创建成功之后需要重新刷新数据
                listener.onOperateCreate(mFolder, creater, CREATE_SUCCESS);
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtil.i("TAG", "onFailure: " + i + " " + s);
                listener.onOperateCreate(mFolder, creater, CREATE_FALLS);
            }
        });
    }

    /**
     * Folder 的建造器
     *
     * @since 1.1
     */
    public static class AddOperater extends FolderOperater {

        /**
         * @param folder 传入null即可
         */
        public AddOperater(@Nullable Folder folder) {
            super(folder);
        }
    }

    /**
     * 查询器
     *
     * @since 1.1
     */
    public static class QueryOperater extends FolderOperater {
        public QueryOperater(Folder folder) {
            super(folder);
        }
    }

    /**
     * 更新器
     */
    public static class UpdateOperater extends FolderOperater {

        public UpdateOperater(Folder folder) {
            super(folder);
        }

        /**
         * 移除当前文件夹下的某个参与者
         *
         * @param folder   目标文件夹
         * @param user     要被移除参与权限的用户
         * @param listener 移除监听器 如果移除成功则返回被移除的用户，否则返回null
         */
        public void removeWorkder(Folder folder, User user, OnUpdateListener<User> listener) {
            folder.getWorkers().remove(user);
            folder.setWorkers(folder.getWorkers());
            folder.update(mContext, new UpdateListener() {
                @Override
                public void onSuccess() {
                    listener.onUpdateFinished(user);
                }

                @Override
                public void onFailure(int i, String s) {
                    listener.onUpdateFinished(null);
                }
            });
        }
    }
}