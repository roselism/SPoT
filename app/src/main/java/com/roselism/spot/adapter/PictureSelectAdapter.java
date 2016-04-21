package com.roselism.spot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.roselism.spot.R;
import com.roselism.spot.adapter.viewholder.CommonViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hero2 on 2016/1/24.
 */
public class PictureSelectAdapter extends CommonAdapter<String> {

    public static List<String> mSelectedImage = new LinkedList<String>(); // 用户选择的图片，存储为图片的完整路径
    private String mDirPath; // 文件夹路径

    public PictureSelectAdapter(Context context, List<String> mDatas, int itemLayoutId, String dirPath) {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
    }

    @Override
    public void convert(final CommonViewHolder helper, final String item) {

        helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no); //设置no_pic
        helper.setImageResource(R.id.id_item_select, R.drawable.picture_unselected);//设置no_selected
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);//设置图片

        final ImageView mImageView = helper.getView(R.id.id_item_image);
        final ImageView mSelect = helper.getView(R.id.id_item_select);

        mImageView.setColorFilter(null);
        //设置ImageView的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            //选择，则将图片变暗
            @Override
            public void onClick(View v) {

                if (mSelectedImage.contains(mDirPath + "/" + item)) {// 已经选择过该图片
                    mSelectedImage.remove(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.picture_unselected);
                    mImageView.setColorFilter(null);
                } else { // 未选择该图片
                    mSelectedImage.add(mDirPath + "/" + item);
                    mSelect.setImageResource(R.drawable.pictures_selected);
                    mImageView.setColorFilter(Color.parseColor("#77000000"));
                }
            }
        });

        if (mSelectedImage.contains(mDirPath + "/" + item)) { //已经选择过的图片，显示出选择过的效果
            mSelect.setImageResource(R.drawable.pictures_selected);
            mImageView.setColorFilter(Color.parseColor("#77000000"));
        }
    }
}
