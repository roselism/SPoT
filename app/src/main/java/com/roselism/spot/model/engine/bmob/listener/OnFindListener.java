package com.roselism.spot.model.engine.bmob.listener;


import com.roselism.spot.model.db.dao.listener.OnLoadListener;
import com.roselism.spot.util.LogUtil;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 对于FindeListener的一些改进
 * 默认实现了 onError和 onSuccess方法
 * 如果只是查询并直接将数据通过OnLoadListener返回，可以使用本方法代替FindListener
 * <note>但如果需要在 onError和 onSuccess中执行更复杂的逻辑，请使用FindListener</note>
 * <p>
 * Created by simon on 16-4-16.
 */
public class OnFindListener extends FindListener {

    OnLoadListener onLoadListener;

    public OnFindListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public void onError(int i, String s) { // 查询错误
        LogUtil.i("错误码：" + i + "错误信息：" + s); // 打印出错误信息
        onLoadListener.onLoadFinished(null); // 回掉
    }

    @Override
    public void onSuccess(List list) { // 查询成功
        LogUtil.i("OnFindListener: " + "查询成功"); // 打印查询成功
        onLoadListener.onLoadFinished(list); // 回掉listener
    }
}