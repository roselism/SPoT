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
import com.roselism.spot.model.domain.bmob.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 好友适配器
 * Created by hero2 on 2016/3/11.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewholder> {

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
    public ContactsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.list_item_contacts, null);
        ContactsViewholder contactsViewholder = new ContactsViewholder(convertView);
        return contactsViewholder;
    }

    @Override
    public void onBindViewHolder(ContactsViewholder holder, int position) {
        User user = (User) mData.get(position);

        holder.friendName.setText(user.getNickName());
        holder.signature.setText("闭月羞花 沉鱼落雁 上得了厅堂 下得了厨房");
        holder.colorBar.setBackgroundColor(mContext.getResources().getColor(R.color.pomegranate));
    }

    public class ContactsViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.profile) ImageView profile;
        @Bind(R.id.color_bar) ImageView colorBar;
        @Bind(R.id.friend_name) TextView friendName;
        @Bind(R.id.signature) TextView signature;
        @Bind(R.id.info_layout) RelativeLayout infoLayout;

        public ContactsViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}