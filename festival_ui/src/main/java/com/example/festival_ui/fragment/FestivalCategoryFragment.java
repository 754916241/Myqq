package com.example.festival_ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.example.festival_ui.ChooseMessage;
import com.example.festival_ui.R;
import com.example.festival_ui.bean.FesitivalLab;
import com.example.festival_ui.bean.Festival;

/**
 * Created by wyj on 2016/6/27.
 */
public class FestivalCategoryFragment extends Fragment{


    private GridView gv;
    private ArrayAdapter<Festival> adapter;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


     return  inflater.inflate(R.layout.fragment_festival_category,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        gv = (GridView) view.findViewById(R.id.gv_festival_category);
        gv.setAdapter(adapter = new ArrayAdapter<Festival>(getActivity(),-1,
                FesitivalLab.getInstance().getFestivals()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView = inflater.inflate(R.layout.item_festival,parent,false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.tv_festival_name);
                tv.setText(getItem(position).getName());
                return convertView;
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChooseMessage.class);
                intent.putExtra("festivalId",adapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }
}
