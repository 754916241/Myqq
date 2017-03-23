package com.example.festival_ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.festival_ui.bean.FesitivalLab;
import com.example.festival_ui.bean.Msg;

public class ChooseMessage extends AppCompatActivity {

    private ListView lv;
    private FloatingActionButton floatToSend;
    private ArrayAdapter<Msg> adapter;
    private int festivalId;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_message);
        inflater = LayoutInflater.from(this);
        festivalId = getIntent().getIntExtra("festivalId",-1);
        setTitle(FesitivalLab.getInstance().getFestivalById(festivalId).getName());
        initViews();
        initEvent();
    }

    private void initEvent() {
        floatToSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg.toActivity(ChooseMessage.this,festivalId,-1);
            }
        });
    }

    private void initViews() {
        lv = (ListView) findViewById(R.id.lv_choosemessage);
        floatToSend = (FloatingActionButton) findViewById(R.id.float_choosemessage_tosend);

        lv.setAdapter(adapter = new ArrayAdapter<Msg>(this,-1,
                FesitivalLab.getInstance().getMsgByFestivalId(festivalId)){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = inflater.inflate(R.layout.item_msg,parent,false);
                }
                TextView content = (TextView) convertView.findViewById(R.id.tv_msg_content);
                Button toSend = (Button)convertView.findViewById(R.id.btn_item_msg_tosend);
                content.setText("    "+getItem(position).getContent());
                toSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendMsg.toActivity(ChooseMessage.this,festivalId,getItem(position).getId());
                    }
                });
                return convertView;
            }
        });
    }
}
