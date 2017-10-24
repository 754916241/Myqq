package com.wyj.myqq.adapter;

import android.content.Context;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;

import java.util.List;

/**
 * Created by wyj on 2016/7/7.
 */
public class ContactsAdapter extends MyAdapter<Friends> {

    public ContactsAdapter(Context context, int layoutId, List<Friends> friendsList) {
        super(context, layoutId, friendsList);
    }

    @Override
    public void convert(ViewHolder holder, Friends friends) {
        holder.setText(R.id.tv_nick,friends.getFriendNick())
                .setImageUseGlide(R.id.img_head,friends.getFriendImg());
    }
}
