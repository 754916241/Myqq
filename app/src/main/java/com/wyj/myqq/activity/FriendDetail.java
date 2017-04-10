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
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.DataCleanManager;
import com.wyj.myqq.utils.ImageUtils;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;

import static com.example.wyj.myqq.R.id.img;

public class FriendDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;

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
    private String qqnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        Config.setNotificationBar(this,R.color.colorApp);
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
        qqnumber = App.user.getQQnumber();
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
                            friends.getFriendNick());
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
                        deleteFriend();
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

    private void deleteFriend() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER,qqnumber);
        params.add(Constant.KEY_FRIENDS_QQNUMBER,friends.getFriendQQ());
        client.post(Constant.HTTPURL_DELETE_FRIEND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getInt("success") == 0){
                        setResult(Constant.RESULT_CODE_DELETE_FRIEND);
                        MyToast.showToast(FriendDetail.this,"删除好友成功",R.mipmap.right, Toast.LENGTH_SHORT);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                MyToast.showToast(FriendDetail.this,"内部网络错误请稍后重试",R.mipmap.error, Toast.LENGTH_SHORT);
            }
        });
    }
}
