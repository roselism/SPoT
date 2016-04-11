package com.roselism.spot.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.roselism.spot.R;
import com.roselism.spot.domain.File;
import com.roselism.spot.domain.Folder;
import com.roselism.spot.domain.Photo;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by hero2 on 2016/2/24.
 */
public class ListSwipeAdapter extends BaseSwipeAdapter implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = "ListSwipeAdapter";
    private List<File> mData; // 数据
    private LayoutInflater mInflater; // 图形打气筒
    private Context mContext; // 上下文内容
    public final static List<File> mSelectedItem = new LinkedList<>(); // 选中的item
    private ImageLoader imageLoader; // imageLoader 对象
    private DisplayImageOptions options; // 选项
//    private OnBtnClickL confirmButtonListener = new OnBtnClickL() {
//        @Override
//        public void onBtnClick() {
//
//        }
//    };

    public ListSwipeAdapter(List<File> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        imageLoader = ImageLoader.getInstance();
        options = buildOptions();
        Log.i(TAG, "ListSwipeAdapter: size = " + getCount());
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.list_item_picture, null);
        ViewHolder viewHolder = new ViewHolder(convertView);
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        data2View(position, viewHolder);
        Log.i(TAG, "fillValues: current position = " + position);
    }

    /**
     * 将数据封装进view
     *
     * @param position   当前data的位置
     * @param viewHolder viewholder
     */
    public void data2View(final int position, final ViewHolder viewHolder) {
        final File currentFile = mData.get(position);
        viewHolder.titleText.setText(currentFile.getTitle());
        viewHolder.uploadName.setText(currentFile.getUser().getEmail());
        viewHolder.subtitleText.setText(currentFile.getSubtitle());
        viewHolder.createTimeText.setText(currentFile.getDate().substring(0, currentFile.getDate().indexOf(" ")));

        int currentType = getItemViewType(position);
//        Log.i(TAG, "data2View: current type = " + currentType);
//        Log.i(TAG, "data2View: current file = " + currentFile.toString());
        switch (currentType) { //
            case File.PICTURE_TYPE: // 对图片类型的item进行设置
                imageLoader.displayImage(currentFile.getFileUrl(), viewHolder.thumbnail, options,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        },
                        new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {

                            }
                        });


                final Photo photo = new Photo(currentFile); // 要被删除的photo对象

                viewHolder.deleteButton.setOnClickListener(view -> { // 删除按钮的点击事件
                    NormalDialog normalDialog = new NormalDialog(mContext).style(NormalDialog.STYLE_TWO); // 设置属性
                    normalDialog.setOnBtnClickL(new ListenerBuilder().cancleButtonListener(), new ListenerBuilder().confimButtonListener(photo, position, viewHolder)); // 设置点击事件
                    normalDialog.show(); // 显示对话框
                });

                viewHolder.editButton.setOnClickListener((view) -> { // editbutton 的点击事件
                });

                break;

            case File.GALLARY_TYPE: // 设置相册类型的item
                Log.i(TAG, "data2View:  gallary type");
                viewHolder.thumbnail.setImageResource(R.mipmap.ic_folder_user_2);

                final Folder folder = new Folder(currentFile); // 要被删除的folder对象
                viewHolder.deleteButton.setOnClickListener((view) -> { // 删除键的监听事件
                    NormalDialog normalDialog = new NormalDialog(mContext);
                    normalDialog.style(NormalDialog.STYLE_TWO);
                    normalDialog.setOnBtnClickL(new ListenerBuilder().cancleButtonListener(), new ListenerBuilder().confimButtonListener(folder, position, viewHolder));
                    normalDialog.show(); // 展示对话框
                });

                viewHolder.editButton.setOnClickListener((view) -> { // editButton 的单击事件
                });

                break;
        }

        viewHolder.editButton.setOnTouchListener(this);
        viewHolder.deleteButton.setOnTouchListener(this);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void in() {

    }

    private DisplayImageOptions buildOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_default_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
        return options;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.edit_button:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.carrot));

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.sun_flower));
                }
                break;

            case R.id.delete_button:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.pomegranate));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundColor(mContext.getResources().getColor(R.color.alizarin));
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_button:

                break;

            case R.id.edit_button:

                break;
        }
    }

    static class ViewHolder {
        // surface layer
        @Bind(R.id.swipeLayout) SwipeLayout swipeLayout;
        @Bind(R.id.bg_image) ImageView bgImage;
        @Bind(R.id.thumbnail) ImageView thumbnail;
        @Bind(R.id.title_text) TextView titleText;
        @Bind(R.id.create_time_text) TextView createTimeText;
        @Bind(R.id.subtitle_text) TextView subtitleText;
        @Bind(R.id.upload_name) TextView uploadName;
        @Bind(R.id.picture_layout) RelativeLayout pictureLayout;
//        @Bind(R.id.btn_selected) ImageView btnSelected;

        // bottom layer
        @Bind(R.id.edit_button) RelativeLayout editButton; // bottom button
        @Bind(R.id.delete_button) RelativeLayout deleteButton; // bottom button
        @Bind(R.id.delete_image) ImageView deleteImage;
        @Bind(R.id.edit_image) ImageView editImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 提供dialog 按钮的监听器
     */
    private class ListenerBuilder {

        /**
         * 确定按钮的监听事件
         *
         * @param data       要被删除的对象
         * @param position   对象的所在位置
         * @param viewHolder viewhoder
         * @return
         */
        public OnBtnClickL confimButtonListener(BmobObject data, int position, ViewHolder viewHolder) {
            return () -> { // 确认按钮
                data.delete(mContext, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        mData.remove(position); // 从本地移除该数据
                        notifyDataSetChanged();
                        viewHolder.swipeLayout.close(true);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(TAG, "onFailure: ");
                    }
                });
            };
        }

        public OnBtnClickL cancleButtonListener() {
            return () -> {
            };
        }
    }
}