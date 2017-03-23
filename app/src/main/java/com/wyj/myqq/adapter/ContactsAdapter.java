package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.ImageUtils;

import java.util.List;

/**
 * Created by wyj on 2016/7/7.
 */
public class ContactsAdapter extends BaseAdapter {
    private List<Friends> friendsList ;
    private LayoutInflater inflater;
    private Bitmap bm;

    public ContactsAdapter(Context context, List<Friends> friendsList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.friendsList = friendsList;
    }
    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Friends getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.contactslist_item,parent,false);
            holder = new ViewHolder();
            holder.friendNick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.friendImg = (ImageView) convertView.findViewById(R.id.img_head);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Friends bean = friendsList.get(position);
        holder.friendNick.setText(bean.getFriendNick());

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    holder.friendImg.setImageBitmap(bm);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                bm = ImageUtils.receiveImage(bean.getFriendImg());
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();

        return convertView;

    }
    private class ViewHolder{
        TextView friendNick;
        ImageView friendImg;
    }

}
