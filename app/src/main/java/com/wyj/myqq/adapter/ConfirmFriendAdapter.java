package com.wyj.myqq.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
 * Created by wyj on 2017/3/25.
 */


public class ConfirmFriendAdapter extends BaseAdapter{


    private ArrayList<ConfirmFriendBean> list;
    private LayoutInflater inflater;
    private volatile Bitmap bm;
    private boolean isAccept;

    public ConfirmFriendAdapter(Context context, ArrayList<ConfirmFriendBean> list) {
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
            convertView = inflater.inflate(R.layout.item_confirm_friend, parent, false);
            holder = new ViewHolder();
            holder.tvResult = (TextView) convertView.findViewById(R.id.tv_result);
            holder.friendNick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.friendImg = (ImageView) convertView.findViewById(R.id.img_head);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            holder.btnAccept = (Button) convertView.findViewById(R.id.btn_accept);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ConfirmFriendBean friends = list.get(position);
        holder.friendNick.setText(friends.getFriendNick() + "(" + friends.getFriendQQ() + ")");
        holder.tvMessage.setText(friends.getApplyMessage());
        if(isAccept){
            holder.btnAccept.setVisibility(View.GONE);
            holder.tvResult.setVisibility(View.VISIBLE);
            holder.tvResult.setText("已同意");
        }

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
        holder.btnAccept.setOnClickListener(new MyClickListener(position));

        return convertView;
    }


    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }


    private class ViewHolder {
        TextView friendNick, tvMessage,tvResult;
        ImageView friendImg;
        Button btnAccept;

    }

    private OnConfirmFriendListener listener;

    public void setOnConfirmListener(OnConfirmFriendListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmFriendListener{
        void click(View v,int position);
    }

    class MyClickListener implements View.OnClickListener {
        private int position;
        public MyClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            listener.click(v,position);
        }
    }
}
