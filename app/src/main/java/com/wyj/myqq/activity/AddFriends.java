package com.wyj.myqq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;
import com.wyj.myqq.utils.ScreenManager;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.wyj.myqq.R.id.img;

public class AddFriends extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title;
    private ImageView imgHead;

    private TextView tvNick;

    private EditText edtExtra;
    /**
     * 确认添加
     */
    private Button btnAddFriend;
    private RelativeLayout activityAddFriends;
    private Bundle bundle;
    private String qqnumber,nickname, imgPath;
    private Friends friends;
    private InputMethodManager imm;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Config.setNotificationBar(this,R.color.colorApp);
        initData();
        initView();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        friends = (Friends) bundle.getSerializable(Constant.KEY_FRIENDS);
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        nickname = friends.getFriendNick();
        imgPath = friends.getFriendImg();
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        imgHead = (ImageView) findViewById(R.id.img_head);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        edtExtra = (EditText) findViewById(R.id.edt_extra);
        btnAddFriend = (Button) findViewById(R.id.btn_add_friend);
        btnAddFriend.setOnClickListener(this);
        activityAddFriends = (RelativeLayout) findViewById(R.id.activity_add_friends);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog = new ProgressDialog(this);
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("确认添加");
        tvNick.setText(nickname);
        Glide.with(this)
                .load(imgPath)
                .placeholder(R.drawable.qq_icon)
                .crossFade()
                .into(imgHead);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                imm.hideSoftInputFromWindow(edtExtra.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_add_friend:
                dialog.setMessage("正在发送验证申请，请稍后");
                dialog.show();
                addFriends();
                dialog.dismiss();
                break;
        }
    }

    private void addFriends() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        params.add(Constant.KEY_FRIENDS_QQNUMBER, friends.getFriendQQ());
        params.add(Constant.KEY_APPLY_MESSAGE,edtExtra.getText().toString());
        client.post(Constant.HTTPURL_ADDFRIENDS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.d("addfriend_response",response);
                int success;
                try {
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if (success == 0) {
                        ScreenManager.getScreenManager().popAllActivityExceptOne(SearchFriends.class);
                        MyToast.showToast(AddFriends.this,"好友申请已发出，请等待对方答复",Toast.LENGTH_SHORT);
                        finish();
                    } else if(success == 1) {
                        MyToast.showToast(AddFriends.this,"该用户已经是您的好友了，不可重复添加",Toast.LENGTH_SHORT);
                    }else{
                        MyToast.showToast(AddFriends.this, "外网错误请稍后重试", Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                MyToast.showToast(AddFriends.this,"内网错误请稍后重试",Toast.LENGTH_SHORT);
            }

        });

    }
}
