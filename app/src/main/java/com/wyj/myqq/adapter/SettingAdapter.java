package com.wyj.myqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.User;
import java.util.List;

/**
 * Created by wyj on 2016/7/4.
 */
public class SettingAdapter extends BaseAdapter {


    private List<User> users ;
    private LayoutInflater inflater;

    public SettingAdapter(Context context,List<User> users) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.settinglist_item,parent,false);
            holder = new ViewHolder();
            holder.nickname = (TextView) convertView.findViewById(R.id.tv_settingitem_nick);
            holder.truename = (TextView) convertView.findViewById(R.id.tv_settingitem_true);
            holder.sex = (TextView) convertView.findViewById(R.id.tv_settingitem_sex);
            holder.age = (TextView) convertView.findViewById(R.id.tv_settingitem_age);
            holder.signature = (TextView) convertView.findViewById(R.id.tv_settingitem_signature);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        User bean = users.get(position);
        holder.nickname.setText(bean.getNickname());
        holder.truename.setText(bean.getTruename());
        holder.age.setText(bean.getAge());
        holder.sex.setText(bean.getSex());
        holder.signature.setText(bean.getSignature());
        return convertView;
    }

    private class ViewHolder{
        TextView nickname,truename,sex,age,signature;
    }
}
