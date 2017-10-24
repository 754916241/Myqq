package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/24.
 */

public class SearchFriendResultAdapter extends BaseAdapter {

    private ArrayList<Friends> list ;
    private LayoutInflater inflater;
    private Bitmap bm;
    private Context context;

    public SearchFriendResultAdapter(Context context,ArrayList<Friends> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Friends getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_searchfriend_result,parent,false);
            holder = new ViewHolder();
            holder.friendNick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.friendImg = (ImageView) convertView.findViewById(R.id.img_head);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Friends friends = list.get(position);
        holder.friendNick.setText(friends.getFriendNick()+"("+friends.getFriendQQ()+")");
       /* final Handler handler = new Handler(){
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
                bm = ImageUtils.receiveImage(friends.getFriendImg());
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();*/
        Glide.with(context)
                .load(friends.getFriendImg())
                .placeholder(R.drawable.qq_icon)
                .crossFade()
                .into(holder.friendImg);
        return convertView;
    }

    private class ViewHolder{
        TextView friendNick;
        ImageView friendImg;
    }
}
