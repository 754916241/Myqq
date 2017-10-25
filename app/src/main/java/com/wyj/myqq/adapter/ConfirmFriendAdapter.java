package com.wyj.myqq.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.ConfirmFriendBean;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/25.
 */


public class ConfirmFriendAdapter extends MyAdapter<ConfirmFriendBean>{


    private boolean isAccept = false;


    public ConfirmFriendAdapter(Context context,int layoutId, ArrayList<ConfirmFriendBean> list) {
        super(context,layoutId,list);
    }

    @Override
    public void convert(com.wyj.myqq.adapter.ViewHolder holder, ConfirmFriendBean friend) {
        holder.setText(R.id.tv_nick,friend.getFriendNick() + "(" + friend.getFriendQQ() + ")")
                .setText(R.id.tv_message,friend.getApplyMessage())
                .setImageUseGlide(R.id.img_head,friend.getFriendImg());
        TextView tvResult = holder.getView(R.id.tv_result);
        Button btnAccept = holder.getView(R.id.btn_accept);
        if(isAccept){
            btnAccept.setVisibility(View.GONE);
            tvResult.setVisibility(View.VISIBLE);
            tvResult.setText("已同意");
        }
        btnAccept.setOnClickListener(new MyClickListener(holder.position));
    }


    public void setAccept(boolean accept) {
        isAccept = accept;
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
