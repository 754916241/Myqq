package com.wyj.myqq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 180321 on 2017/10/24.
 */

public abstract class MyAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> datas;
    protected LayoutInflater inflater;
    protected int layoutId;

    public MyAdapter(Context context,int layoutId,List<T> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = ViewHolder.get(context,view,viewGroup,layoutId,i);
        convert(holder,getItem(i));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder,T t);
}
