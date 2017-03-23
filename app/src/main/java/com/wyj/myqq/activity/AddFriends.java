package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.MyToast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFriends extends AppCompatActivity {

    private EditText edtAddFriend;
    private TextView tvAdd,tvCancel;
    private String qqnumber;
    private int success;
    private Friends friend;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Bundle bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        init();
    }

    private void init() {

        edtAddFriend = (EditText) findViewById(R.id.edt_add_friend);
        tvCancel = (TextView) findViewById(R.id.tv_left);
        tvAdd = (TextView) findViewById(R.id.tv_right);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qqnumber.equals(edtAddFriend.getText().toString())) {
                    MyToast.showToast(AddFriends.this, "不能添加自己为好友哦", Toast.LENGTH_SHORT);
                } else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add(Constant.KEY_QQNUMBER, qqnumber);
                    params.add(Constant.KEY_FRIENDS_QQNUMBER, edtAddFriend.getText().toString());
                    client.post(Constant.HTTPURL_ADDFRIENDS, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            String response = new String(bytes);
                            System.out.println(response);
                            try {
                                JSONArray array = new JSONArray(response);
                                JSONObject object = array.getJSONObject(0);
                                success = object.getInt("success");
                                System.out.println(object);

                                if (success == 0) {
                                    friend = new Friends( object.getString("qqnumber"),
                                             object.getString(Constant.KEY_NICK),
                                             object.getString(Constant.KEY_TOKEN),
                                             object.getString(Constant.KEY_SIGNATURE),
                                             object.getString(Constant.KEY_IMAGE));

                                    Intent intent = new Intent();
                                    intent.putExtra(Constant.KEY_FRIENDS_ITEM, friend);
                                    setResult(Constant.RESULT_CODE_ADDFRIENDS, intent);
                                    Toast.makeText(AddFriends.this, "正在等待对方通过验证", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (success == 1) {
                                    Toast.makeText(AddFriends.this, "该用户已经是您的好友了，不可重复添加", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddFriends.this, "您搜索的用户不存在，请核对后重试", Toast.LENGTH_SHORT).show();
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
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(edtAddFriend.getWindowToken(), 0);
                onBackPressed();
            }
        });

    }


}
