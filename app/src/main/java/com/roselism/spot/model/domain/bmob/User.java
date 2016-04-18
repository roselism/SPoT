package com.roselism.spot.model.domain.bmob;

import android.content.Context;
import android.widget.Toast;

import com.roselism.spot.model.domain.local.Image;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by hero2 on 2016/1/25.
 * 请不要做任何修改！
 */
public class User extends BmobUser {
    private String nickName;
    private Image profile;

    public User() {
    }

    public User(String nickName, Image profile) {
        this.nickName = nickName;
        this.profile = profile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfile(Image profile) {
        this.profile = profile;
    }


    public Image getProfile() {
        return profile;
    }

    /**
     * 当前用户退出某个相册组
     *
     * @param context 上下文对象
     * @param folder  要退出的相册组对象
     */
    public void quite(final Context context, final Folder folder) {
        BmobRelation relation = new BmobRelation();
        relation.remove(this);
        folder.setWorkers(relation);
        folder.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
//                Log.i(TAG, "onSuccess: 退出成功");
                Toast.makeText(context, "退出" + folder.getName() + "组成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
//                Log.i(TAG, "onFailure:" + i + " " + s);
                Toast.makeText(context, "退出" + folder.getName() + "组失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}