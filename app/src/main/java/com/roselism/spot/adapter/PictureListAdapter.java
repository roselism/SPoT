package com.roselism.spot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.roselism.spot.R;
import com.roselism.spot.model.domain.File;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hero2 on 2016/1/25.
 *
 * @deprecated list item使用了SwipeLayout，所以请使用最新的适配器
 */
public class PictureListAdapter extends BaseAdapter {
    private List<File> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    public static List<File> mSelectedItem;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private File currentFile; // 当前的File对象

    private static final String TAG = "PictureListAdapter";

    public PictureListAdapter(List<File> mData, Context mContext) {
        this.mData = new LinkedList<>();
        this.mData = mData;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);

        // 初始化成员变量
        mSelectedItem = new LinkedList<>();

        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .diskCacheFileCount(30)
                .build();
        imageLoader.init(configuration);
        options = buildOptions();
    }

    /**
     * listview当中的第一个列表项被选中的监听器
     */
    public interface OnItemClickListener {
        /**
         * 当列表中第一次有item被选中时回调</br>
         * 以下情景中该方法将会回掉： </br>
         * 1. listview中第一次有选项被选中</br>
         * 2. listview中有选项被选中，但是都被取消，之后选中新的一项时</br>
         *
         * @param view
         */
        void onFirstItemClick(View view);

        /**
         * 当所有的item都被取消选中时，回调该函数
         */
        void onNoItemSelected();
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

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_picture, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        currentFile = mData.get(position);

        data2View(position, viewHolder);

        return convertView;
    }

    /**
     * 将数据封装进view
     *
     * @param position   当前data的位置
     * @param viewHolder viewholder
     */
    public void data2View(final int position, ViewHolder viewHolder) {
        currentFile = mData.get(position);

        int currentType = getItemViewType(position);
        switch (currentType) { //
            case File.PICTURE_TYPE: // 对图片类型的item进行设置
                imageLoader.displayImage(currentFile.getFileUrl(), viewHolder.preImage, options);
                break;
            case File.GALLARY_TYPE: // 设置相册类型的item
                viewHolder.preImage.setImageResource(R.mipmap.ic_folder_user);
                break;
        }
        viewHolder.title.setText(currentFile.getTitle());
        viewHolder.creater.setText(currentFile.getUser().getEmail());
        viewHolder.subtitle.setText(currentFile.getSubtitle());
        viewHolder.createDate.setText(currentFile.getDate().substring(0, currentFile.getDate().indexOf(" ")));
//        viewHolder.selectButton.setImageResource(isInSelectedFile(currentFile) ? R.mipmap.ic_check_on : R.mipmap.ic_uncheck_on);
//        viewHolder.selectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick: id=" + currentFile.getId());
//                PictureListAdapter.this.onClick(v, mData.get(position));
//            }
//        });
    }

    /**
     * 重载Onclick方法，用于更新当前File对象 currentFile
     *
     * @param v           被单击的view
     * @param currentFile 当前的数据源，file
     */
    public void onClick(View v, File currentFile) {
        this.currentFile = currentFile;
        doCallBack(v);
    }

    /**
     * file 是否在被选择
     *
     * @param file 要被检查的file
     * @return 如果被包含，则返回true，反之返回false
     */
    public static boolean isInSelectedFile(File file) {
        boolean isExist = false; // 是否存在

        if (mSelectedItem.contains(file)) { // 如果包含该view，则说明之前已经选中
            isExist = true;
            Log.i(TAG, "isInSelectedFile: 包含 file id=" + file.getId());
        } else {
            for (File f : mSelectedItem) {
                if (f.getId().equals(file.getId())) { // 判断id是否相同
                    isExist = true;
                    Log.i(TAG, "isInSelectedFile: 遍历 file id=" + file.getId());
                }
            }
        }
        return isExist;
    }

    /**
     * 执行添加或删除方法，并且如果满足条件的话调用回调函数
     *
     * @param v
     */
    void doCallBack(View v) {
        OnItemClickListener itemClickListener = (OnItemClickListener) mContext;
        if (addOrRemoveView(v, currentFile)) { // 是否是移除操作
            if (mSelectedItem.size() == 0) { // 判断当前选中的item的数量
                itemClickListener.onNoItemSelected(); // 触发没有选项被选中的回调函数
                Log.i(TAG, "onClick: ------当前操作------");
            }
        } else { // 当前为添加操作
            if (mSelectedItem.size() == 1) { // 添加过之后size为2，则说明当前选项为第二个选中项
                Log.i(TAG, "onClick: ++++++当前操作++++++");
                itemClickListener.onFirstItemClick(v); // 触发第一个选中项被选中的函数
            }
        }
    }

    /**
     * 从选中列表里添加或者移除某一项
     * 如果已经选中，则移除
     * 如果没有选中，则添加
     *
     * @param view 被单击的view
     * @param file 与view对应的file(data)
     * @return 如果已经存在，当前操作为移除则返回true，反之返回false；
     */
    public static boolean addOrRemoveView(View view, File file) {
        boolean isExist; // 是否存在
        isExist = isInSelectedFile(file);

        if (isExist) { // 已经存在，执行移除操作
            ((ImageView) view).setImageResource(R.mipmap.ic_round_dot); // 更换图片
            mSelectedItem.remove(file); // 移除file
            Log.i(TAG, "RemoveView: current size:" + mSelectedItem.size());
            return true;
        } else { // 不存在，执行添加操作
            ((ImageView) view).setImageResource(R.mipmap.ic_check_on);
            mSelectedItem.add(file);
            Log.i(TAG, "addView: current size:" + mSelectedItem.size());
            return false;
        }
    }

    /**
     * 移除所有当前选中的File
     */
    public static void removeAll() {
        for (File f : mSelectedItem) {

        }
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'main_list_itemxml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public static class ViewHolder {
        @Bind(R.id.thumbnail) public ImageView preImage;
        @Bind(R.id.title_text) public TextView title; // title
        @Bind(R.id.subtitle_text) public TextView subtitle; // subtitle
        @Bind(R.id.create_time_text) public TextView createDate; // 创造日期
        @Bind(R.id.upload_name) public TextView creater; // 创作者
//        @Bind(R.id.btn_selected) public ImageView selectButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
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
}