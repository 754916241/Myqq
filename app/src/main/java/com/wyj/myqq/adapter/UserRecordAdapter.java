package com.wyj.myqq.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Constant;

import java.util.List;
import java.util.Map;


public class UserRecordAdapter extends MyAdapter<Map<String, Object>>{

	public UserRecordAdapter(Context context,int layoutId,List<Map<String, Object>> moreList) {
		super(context,layoutId,moreList);
	}

	@Override
	public void convert(com.wyj.myqq.adapter.ViewHolder holder, Map<String, Object> map) {
		holder.setText(R.id.tv_qqnumber,(String)map.get(Constant.KEY_QQNUMBER))
				.setImageFromBase64(R.id.img_head,(String)map.get(Constant.KEY_IMAGE));
		holder.getView(R.id.img_head).setOnClickListener(new MyClickListener(holder.position));
	}

	private UserRecordClickListener listener;

	public void setUserRecordListener(UserRecordClickListener listener) {
		this.listener = listener;
	}
	
	public interface UserRecordClickListener{
		void click(View v, int position);
	}
	
	class MyClickListener implements OnClickListener{	
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



