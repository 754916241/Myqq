package com.wyj.myqq.adapter;

import android.content.Context;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.ConfirmFriendBean;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/31.
 */

public class DiscussionMembersAdapter extends MyAdapter<ConfirmFriendBean> {

    private ArrayList<ConfirmFriendBean> list;

    public DiscussionMembersAdapter(Context context,int layoutId, ArrayList<ConfirmFriendBean> list) {
        super(context,layoutId,list);
        this.list = list;
    }

    @Override
    public void convert(com.wyj.myqq.adapter.ViewHolder holder, ConfirmFriendBean members) {

        if (holder.position < list.size()-1) {
            holder.setText(R.id.tv_name,members.getFriendNick())
                    .setImageUseGlide(R.id.img_head,members.getFriendImg());
        } else if (holder.position == list.size()-1) {
            holder.setText(R.id.tv_name,"邀请")
                    .setImageResource(R.id.img_head,R.mipmap.dicussion_add);
        }
    }

}
