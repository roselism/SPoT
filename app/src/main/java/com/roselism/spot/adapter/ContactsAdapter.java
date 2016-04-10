package com.roselism.spot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roselism.spot.R;
import com.roselism.spot.domain.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友适配器
 * Created by hero2 on 2016/3/11.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Viewholder> {

    private List<User> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public ContactsAdapter(List<User> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            Log.i("ContactsAdapter", "getItemCount: mData size = " + mData.size());
            return mData.size();
        } else return 0;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.list_item_contacts, null);
        Viewholder viewholder = new Viewholder(convertView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        User user = mData.get(position);

        holder.friendName.setText(user.getNickName());
        holder.signature.setText("这个家伙很勤快，什么都没有留下");
        holder.colorBar.setBackgroundColor(mContext.getResources().getColor(R.color.pomegranate));
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.profile) ImageView profile;
        @Bind(R.id.color_bar) ImageView colorBar;
        @Bind(R.id.friend_name) TextView friendName;
        @Bind(R.id.signature) TextView signature;
        @Bind(R.id.info_layout) RelativeLayout infoLayout;

        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}