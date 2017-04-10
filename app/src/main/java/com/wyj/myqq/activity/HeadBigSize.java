package com.wyj.myqq.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;

public class HeadBigSize extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private RelativeLayout layoutHead;
    private Bitmap bitmap;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_head_big_size);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        layoutHead = (RelativeLayout) findViewById(R.id.layout_head);
        layoutHead.setOnClickListener(this);
        image = getIntent().getStringExtra(Constant.KEY_IMAGE);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    imageView.setImageBitmap(bitmap);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                bitmap = ImageUtils.receiveImage(image);
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_head:
                onBackPressed();
                break;
        }
    }
}
