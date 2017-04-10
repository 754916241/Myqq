package com.wyj.myqq.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Config;

import io.rong.imkit.RongIM;

import static com.example.wyj.myqq.R.id.img;

public class About extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.img_left);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("关于");
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
