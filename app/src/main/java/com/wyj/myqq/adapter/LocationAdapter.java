package com.wyj.myqq.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.AddressBean;

import java.util.List;

/**
 * Created by 180321 on 2017/11/7.
 */
public class LocationAdapter extends MyAdapter<AddressBean>{

    //存储每一个position与是否被选中的关系
    private SparseBooleanArray isChecked ;

    //如果第一次进入，则第一个选项默认亮起，之后重绘listview即为false
    private boolean isFirstEnter = true;

    public LocationAdapter(Context context, int layoutId, List<AddressBean> datas) {
        super(context, layoutId, datas);
        isChecked = new SparseBooleanArray();
    }

    @Override
    public void convert(final ViewHolder holder, AddressBean bean) {
        holder.setText(R.id.tv_description,bean.getDescription())
                .setText(R.id.tv_address,bean.getAddress());
        if(isFirstEnter){
            if(holder.position == 0) {
                isChecked.put(holder.position,true);
            }else{
                isChecked.put(holder.position,false);
            }
        }

        if(isChecked.get(holder.position)){
            holder.setTextColor(R.id.tv_description,R.color.colorPrimary)
                    .setTextColor(R.id.tv_address,R.color.colorPrimary);
            holder.getView(R.id.img_select_location).setVisibility(View.VISIBLE);
        }else{
            holder.setTextColor(R.id.tv_description,R.color.colorBlack)
                    .setTextColor(R.id.tv_address,R.color.colorBlack);
            holder.getView(R.id.img_select_location).setVisibility(View.GONE);
        }

        isFirstEnter = false;
        holder.getView(R.id.layout_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastPosition = isChecked.indexOfValue(true);
                Log.d("LOCATIONADAPTER",lastPosition+"");
                isChecked.put(lastPosition,false);
                isChecked.put(holder.position,true);
                notifyDataSetChanged();
            }
        });
    }
}
