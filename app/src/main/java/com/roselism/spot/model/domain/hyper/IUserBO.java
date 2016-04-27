package com.roselism.spot.model.domain.hyper;

/**
 * Created by simon on 2016/4/21.
 * BO -> business Object 业务对象
 */
public interface IUserBO {

    /**
     * 获取用户的id
     *
     * @return
     */
    public String getId();

    /**
     * 获取用户对象创建的时间
     *
     * @return
     */
    public String getCreateAt();

    /**
     * 获取昵称
     *
     * @return
     */
    public String getNickName();

    /**
     * 获取email
     *
     * @return
     */
    public String getEmail();

    /**
     * username必须唯一，所以与email相同
     *
     * @return
     */
    public String getUsername();

    /**
     * 获取用户的密码
     *
     * @return
     */
    public String getPassword();
}