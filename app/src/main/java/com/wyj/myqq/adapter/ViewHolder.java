package com.wyj.myqq.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wyj.myqq.R;

/**
 * Created by 180321 on 2017/10/24.
 */

public class ViewHolder{

    protected int position;
    private SparseArray<View> views;
    private View convertView;
    private Context context;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.position = position;
        this.views = new SparseArray<>();
        convertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        convertView.setTag(this);
        this.context = context;
    }

    public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
        if(convertView == null){
            return  new ViewHolder(context,parent,layoutId,position);
        }else{
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.position = position;
            return holder;
        }
    }

    private <T extends View> T getView(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = convertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T)view;
    }

    public View getConvertView(){
        return convertView;
    }

    /**
     * 链式编程
     * @return
     */
    public ViewHolder setText(int viewId,String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageUseGlide(int viewId, String imageResource){
        ImageView img = getView(viewId);
        Glide.with(context)
                .load(imageResource)
                .placeholder(R.drawable.qq_icon)
                .crossFade()
                .into(img);
        return this;
    }
}
