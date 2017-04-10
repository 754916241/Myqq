package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.utils.ImageUtils;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/31.
 */

public class DiscussionMembersAdapter extends BaseAdapter {
    private ArrayList<ConfirmFriendBean> list;
    private LayoutInflater inflater;
    private Bitmap bm;
    private Handler handler;

    public DiscussionMembersAdapter(Context context, ArrayList<ConfirmFriendBean> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ConfirmFriendBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_discussion_members, parent, false);
            holder = new ViewHolder();
            holder.membersNick = (TextView) convertView.findViewById(R.id.tv_name);
            holder.membersImg = (ImageView) convertView.findViewById(R.id.img_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position < list.size()-1) {
            final ConfirmFriendBean members = list.get(position);
            holder.membersNick.setText(members.getFriendNick());

            new AsyncTask<String,Void,Bitmap>(){

                @Override
                protected Bitmap doInBackground(String... params) {
                    bm = ImageUtils.receiveImage(members.getFriendImg());
                    return bm;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {

                    holder.membersImg.setImageBitmap(bitmap);
                }
            }.execute();

        } else if (position == list.size()-1) {
            holder.membersNick.setText("邀请");
            holder.membersImg.setImageResource(R.mipmap.dicussion_add);
        }


        return convertView;
    }

    private class ViewHolder {
        TextView membersNick;
        ImageView membersImg;
    }
}
