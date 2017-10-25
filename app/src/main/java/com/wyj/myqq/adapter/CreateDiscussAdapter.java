package com.wyj.myqq.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;

import java.util.ArrayList;

/**
 * Created by wyj on 2017/3/25.
 */


public class CreateDiscussAdapter extends MyAdapter<Friends>{


    public ArrayList<Friends> getChooseList() {
        return chooseList;
    }
    private ArrayList<Friends> chooseList;

    public CreateDiscussAdapter(Context context,int layoutId, ArrayList<Friends> list) {
        super(context,layoutId,list);
        chooseList = new ArrayList<>();
    }

    @Override
    public void convert(com.wyj.myqq.adapter.ViewHolder holder, final Friends friends) {
        holder.setText(R.id.tv_nick,friends.getFriendNick())
                .setImageUseGlide(R.id.img_head,friends.getFriendImg());
        ((CheckBox)holder.getView(R.id.cb_choose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    chooseList.add(friends);
                }else{
                    chooseList.remove(friends);
                }
            }
        });
    }


}
