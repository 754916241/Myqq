package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
    private volatile Bitmap bm;

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

        new AsyncTask<Void,Void,Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... params) {
                bm = ImageUtils.receiveImage(bean.getFriendImg());
                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                holder.friendImg.setImageBitmap(bitmap);
            }
        }.execute();

        return convertView;

    }
    private class ViewHolder{
        TextView friendNick;
        ImageView friendImg;
    }

}
