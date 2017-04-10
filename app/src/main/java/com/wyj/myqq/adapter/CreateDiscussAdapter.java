package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.ImageUtils;


import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/25.
 */


public class CreateDiscussAdapter extends BaseAdapter{


    private ArrayList<Friends> list;

    public ArrayList<Friends> getChooseList() {
        return chooseList;
    }

    private ArrayList<Friends> chooseList;
    private LayoutInflater inflater;
    private Bitmap bm;


    public CreateDiscussAdapter(Context context, ArrayList<Friends> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        chooseList = new ArrayList<>();
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_create_discuss, parent, false);
            holder = new ViewHolder();
            holder.cbChoose = (CheckBox) convertView.findViewById(R.id.cb_choose);
            holder.friendNick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.friendImg = (ImageView) convertView.findViewById(R.id.img_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Friends friends = list.get(position);
        holder.friendNick.setText(friends.getFriendNick());

        new AsyncTask<Void,Void,Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... params) {
                bm = ImageUtils.receiveImage(friends.getFriendImg());
                return bm;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                holder.friendImg.setImageBitmap(bitmap);
            }
        }.execute();

        holder.cbChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chooseList.add(friends);
                }else{
                    chooseList.remove(friends);
                }
            }
        });

        return convertView;
    }


    private class ViewHolder {
        TextView friendNick;
        ImageView friendImg;
        CheckBox cbChoose;

    }

}
