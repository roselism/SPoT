package com.roselism.spot.model.dao.operator;

import android.content.Context;

import com.roselism.spot.SPoTApplication;
import com.roselism.spot.model.domain.bmob.Folder;
import com.roselism.spot.model.domain.bmob.User;

/**
 * 所有操作类的父类
 * <p>
 * Created by hero2 on 2016/3/6.
 *
 * @deprecated
 */
public abstract class Operater {

    /**
     * @deprecated
     */
    public static final int CREATE_SUCCESS = 0;

    /**
     * @deprecated
     */
    public static final int CREATE_FALLS = 1;

    //    private Folder mFolder;
    protected static Context mContext;

    public Operater() {
        initContext();
    }

    /**
     * 初始化context对象
     */
    /**
     * 初始化上下文对象
     */
    protected static void initContext() {
        mContext = SPoTApplication.getContext();
    }

    /**
     * 操作监听器
     *
     * @deprecated 使用 OnBuildListener OnDeleteListener OnLoadListener OnUpdateListener替代
     */
    public interface onOperatListener {

        /**
         * 当文件夹创建时
         *
         * @param folder  被创建的文件夹
         * @param creater 创建的用户
         * @param state   状态（创建成功 | 创建失败）
         */
        void onOperateCreate(Folder folder, User creater, int state);

//        /**
//         * 当操作完成时
//         */
//        void onOperateFinished();
    }
}