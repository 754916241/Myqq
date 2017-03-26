package com.wyj.myqq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;
import com.wyj.myqq.utils.ScreenManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class Login extends AppCompatActivity implements RongIM.UserInfoProvider{

    private Button login,regist;
    private EditText edtQQnumber,edtPassword;
    private Bundle bundle;
    private int success;
    private String qqnumber,nickname, sex, age, truename, signature, token, image,phone;
    private User user;
    private Friends friends;
    private List<Friends> list;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        setContentView(R.layout.activity_login);
        initView();
        bundle = getIntent().getExtras();
        if(bundle!=null){
            edtQQnumber.setText(bundle.getString(Constant.KEY_QQNUMBER));
        }

        RongIM.setUserInfoProvider(this,true);
        initClick();
    }

    private void initClick() {
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Regist.class));
            }
        });

        edtQQnumber.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPassword.setText("");
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("正在验证个人信息，请稍后");
                dialog.show();
                if(list.size()!=0){
                    list.clear();
                }
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                if(edtQQnumber.getText().toString().equals("")||
                        edtPassword.getText().toString().equals("")){
                    MyToast.showToast(Login.this,"qq号或密码不能为空！",Toast.LENGTH_LONG);
                    dialog.dismiss();
                }else{
                    params.add(Constant.KEY_QQNUMBER,edtQQnumber.getText().toString());
                    params.add(Constant.KEY_PASSWORD,edtPassword.getText().toString());
                    client.post(Constant.HTTPURL_LOGIN, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            SharedPreferences sp = getSharedPreferences(
                                    "mysp", Context.MODE_PRIVATE);
                            Editor editor = sp.edit();
                            editor.putString(Constant.KEY_QQNUMBER, edtQQnumber.getText().toString());
                            editor.putString(Constant.KEY_PASSWORD, edtPassword.getText().toString());
                            editor.apply();
                            String result = new String(bytes);
                            try {
                                Log.d("login_result",result);
                                final JSONArray array = new JSONArray(result);
                                JSONObject userObject = array.getJSONObject(0);
                                success = userObject.getInt("success");
                                if(success==0){
                                    qqnumber = userObject.getString(Constant.KEY_QQNUMBER);
                                    nickname = userObject.getString(Constant.KEY_NICK);
                                    truename = userObject.getString(Constant.KEY_TRUE);
                                    phone = userObject.getString(Constant.KEY_PHONE);
                                    token = userObject.getString(Constant.KEY_TOKEN);
                                    sex = userObject.getString(Constant.KEY_SEX);
                                    age = userObject.getString(Constant.KEY_AGE);
                                    signature = userObject.getString(Constant.KEY_SIGNATURE);
                                    image = userObject.getString(Constant.KEY_IMAGE);
                                    user = new User(qqnumber,nickname,truename,sex,age,phone,token,signature,image);
                                    RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                        @Override
                                        public void onTokenIncorrect() {
                                            dialog.dismiss();
                                            Toast.makeText(Login.this,"token错误请稍后重试",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSuccess(String s) {

                                            for (int j = 1;j<array.length();j++){
                                                JSONObject friendsObject ;
                                                try {
                                                    friendsObject = array.getJSONObject(j);
                                                    friends = new Friends(friendsObject.getString(Constant.KEY_FRIENDS_QQNUMBER),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_NICK),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_SEX),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_PHONE),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_TOKEN),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_IMAGE),
                                                            friendsObject.getString(Constant.KEY_FRIENDS_SIGNATURE));
                                                    list.add(friends);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            Intent intent = new Intent(Login.this,MainUI.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constant.KEY_PASSWORD,edtPassword.getText().toString());
                                            bundle.putSerializable(Constant.KEY_USER,user);
                                            if(list.size()!=0){
                                                bundle.putSerializable(Constant.KEY_FRIENDS, (Serializable) list);
                                            }
                                            intent.putExtras(bundle);
                                            dialog.dismiss();
                                            startActivity(intent);

                                        }
                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            dialog.dismiss();
                                            Toast.makeText(Login.this,"外部网络服务器错误请稍后重试",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }else{
                                    dialog.dismiss();
                                    MyToast.showToast(Login.this,"用户名或密码输入错误，请检查后重试",Toast.LENGTH_LONG);
                                }

                            } catch (JSONException e) {
                                dialog.dismiss();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            dialog.dismiss();
                            MyToast.showToast(Login.this,"内部网络错误请稍后重试",R.mipmap.error,Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });

    }

    private void initView() {
        login = (Button) findViewById(R.id.btn_login_login);
        regist = (Button) findViewById(R.id.btn_login_regist);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        edtQQnumber = (EditText) findViewById(R.id.edt_login_qqnumber);
        SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
        edtQQnumber.setText(sp.getString(Constant.KEY_QQNUMBER,""));
        edtPassword.setText(sp.getString(Constant.KEY_PASSWORD,""));
        ScreenManager screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
        list = new ArrayList<>();
        dialog = new ProgressDialog(this);
    }

    @Override
    public UserInfo getUserInfo(String userId) {
//        System.out.println("run here");
//        UserInfo userInfo = new UserInfo(s,"系统管理员",Uri.parse("http://192.168.1.9:82/htdocs/qq/0system.png"));
        if(userId.equals("123456")){
            return new UserInfo(userId,"系统消息",null);
        }
        if(userId.equals("754916241")){
            return new UserInfo(userId,"流年似水",Uri.parse("http://192.168.0.108:82/htdocs/qq/myself.png"));
        }
        return null;
    }
}
