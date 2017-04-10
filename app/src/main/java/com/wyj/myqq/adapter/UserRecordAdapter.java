package com.wyj.myqq.adapter;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;


public class UserRecordAdapter extends BaseAdapter{
	
	private List<Map<String, Object>> list;
	private LayoutInflater inflater;
	

	public UserRecordAdapter(Context context,List<Map<String, Object>> moreList) {
		this.list = moreList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Map<String,Object> getItem(int arg0) {

		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_user_record, parent,false);
			holder = new ViewHolder();
			holder.tvQQnumber = (TextView) convertView.findViewById(R.id.tv_qqnumber);
			holder.imgDelete = (ImageView) convertView.findViewById(R.id.img_delete);
			holder.imgHead = (ImageView) convertView.findViewById(R.id.img_head);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvQQnumber.setText((String)list.get(position).get(Constant.KEY_QQNUMBER));
		holder.imgHead.setImageBitmap(ImageUtils.stringToBitmap(
				(String)list.get(position).get(Constant.KEY_IMAGE)));
		holder.imgDelete.setOnClickListener(new MyClickListener(position));
		return convertView;
	}
	
	

	private class ViewHolder{
		TextView tvQQnumber;
		ImageView imgDelete,imgHead;
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



