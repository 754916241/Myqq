package com.wyj.myqq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.DataCleanManager;
import com.wyj.myqq.utils.ImageUtils;

import io.rong.imkit.RongIM;

import static com.example.wyj.myqq.R.id.img;

public class FriendDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title;
    private ImageView imgHead;

    private TextView tvNick;

    private TextView tvQQnumber;
    private ImageView imgSex;

    private TextView tvSignature,tvPhone;
    /**
     * 发消息
     */
    private Button btnSend;
    /**
     * 删除好友
     */
    private Button btnDelete;
    private Bundle bundle;
    private Friends friends;
    private Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        initView();
        initData();
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        title = (TextView) findViewById(R.id.title);
        imgHead = (ImageView) findViewById(R.id.img_head);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvQQnumber = (TextView) findViewById(R.id.tv_qqnumber);
        imgSex = (ImageView) findViewById(R.id.img_sex);
        tvSignature = (TextView) findViewById(R.id.tv_signature);
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("详细资料");
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
    }

    private void initData() {
        bundle = getIntent().getExtras();
        friends = (Friends) bundle.getSerializable(Constant.KEY_FRIENDS);
        tvQQnumber.setText(friends.getFriendQQ());
        tvNick.setText(friends.getFriendNick());
        tvSignature.setText(friends.getFriendSign());
        tvPhone.setText(friends.getFriendPhone());
        if(friends.getFriendSex().equals("男")){
            imgSex.setImageResource(R.mipmap.icon_man);
        }else{
            imgSex.setImageResource(R.mipmap.icon_woman);
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    imgHead.setImageBitmap(bm);
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                bm = ImageUtils.receiveImage(friends.getFriendImg());
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if(RongIM.getInstance()!=null){
                    RongIM.getInstance().startPrivateChat(this,friends.getFriendQQ(),
                            "与"+friends.getFriendNick()+"聊天中");
                  finish();
                }
                break;
            case R.id.btn_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("系统提示");
                builder.setMessage("确认删除该好友?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.img_left:
                onBackPressed();
                break;
        }
    }
}
