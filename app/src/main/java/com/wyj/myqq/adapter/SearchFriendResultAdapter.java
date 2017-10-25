package com.wyj.myqq.adapter;

import android.content.Context;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/24.
 */

public class SearchFriendResultAdapter extends MyAdapter<Friends> {

    public SearchFriendResultAdapter(Context context,int layoutId,ArrayList<Friends> list) {
        super(context,layoutId,list);
    }

    @Override
    public void convert(com.wyj.myqq.adapter.ViewHolder holder, Friends friends) {
        holder.setText(R.id.tv_nick,friends.getFriendNick()+"("+friends.getFriendQQ()+")")
                .setImageUseGlide(R.id.img_head,friends.getFriendImg());
    }

}
