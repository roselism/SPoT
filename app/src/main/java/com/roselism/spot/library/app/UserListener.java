package com.roselism.spot.library.app;

import cn.bmob.v3.BmobUser;

/**
 * 主要提供当前user对象
 * 为查询当前用户提供统一的接口
 * Created by simon on 16-4-14.
 *
 * @param <T>
 */
public interface UserListener<T extends BmobUser> {

    /**
     * @return 返回当前登录的用户
     */
    T getUser();
}