package com.wyj.myqq.adapter;

import android.content.Context;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.AddressBean;

import java.util.List;

/**
 * Created by 180321 on 2017/11/7.
 */
public class AddressAdapter extends MyAdapter<AddressBean>{


    public AddressAdapter(Context context, int layoutId, List<AddressBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, AddressBean bean) {
        holder.setText(R.id.tv_description,bean.getDescription())
                .setText(R.id.tv_address,bean.getAddress());
    }
}
