package com.roselism.spot.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.roselism.spot.R;
import com.roselism.spot.domain.File;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hero2 on 2016/2/2.
 */
public class PictureGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<File> mData;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    DisplayImageOptions options;

    public PictureGridAdapter(@NonNull Context context, @NonNull List<File> mData) {
        this.mData = mData;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        // 初始化成员变量
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(mContext)
                .diskCacheFileCount(30)
                .build();
        imageLoader.init(configuration);
        options = buildOptions();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_in_folder, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        File curFile = mData.get(position);
        imageLoader.displayImage(curFile.getFileUrl(), viewHolder.photo, options);
        viewHolder.takenDate.setText(curFile.getDate());
        viewHolder.uploaderName.setText(curFile.getUser().getNickName() == null ? curFile.getUser().getUsername() : curFile.getUser().getNickName());

        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mInfoLayout.setVisibility(
                        viewHolder.mInfoLayout.getVisibility() == View.VISIBLE ?
                                View.GONE : View.VISIBLE);
/*
                viewHolder.takenDate.setVisibility(
                        viewHolder.takenDate.getVisibility() == View.VISIBLE ?
                                View.GONE : View.VISIBLE); // 设置为显示

*/
            }
        });
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'grid_item_in_folder.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.photo_imageView) ImageView photo;
        @Bind(R.id.uploader_name_view) TextView uploaderName;
        @Bind(R.id.taken_date_view) TextView takenDate;
        @Bind(R.id.info_layout) RelativeLayout mInfoLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private DisplayImageOptions buildOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_default_pic)
                .delayBeforeLoading(100)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(10))
                .build();
        return options;
    }
}