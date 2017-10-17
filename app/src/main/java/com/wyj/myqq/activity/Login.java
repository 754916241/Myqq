package com.wyj.myqq.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences.Editor;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.adapter.UserRecordAdapter;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.dblocal.DBLocal;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;
import com.wyj.myqq.view.MyToast;
import com.wyj.myqq.utils.ScreenManager;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class Login extends AppCompatActivity implements RongIM.UserInfoProvider,UserRecordAdapter.UserRecordClickListener{

    private Button login,regist;
    private EditText edtQQnumber,edtPassword;
    private Bundle bundle;
    private int success;
    private String qqnumber,nickname, sex, age, truename, signature, token, image,phone;
    private User user;
    private Friends friends;
    private ConfirmFriendBean friendInPanding;
    private ArrayList<Friends> friendsListAgreed;
    private ArrayList<ConfirmFriendBean> friendsListInPanding;
    private ProgressDialog dialog;
    private Bitmap localBm ;
    private ImageView imgHead,imgExpand;
    private View popLayout;
    private PopupWindow popRecord;
    private ListView lvPop;
    private List<Map<String, Object>> popList = new ArrayList<>();
    private UserRecordAdapter adapter;
    private DBLocal db;
    private Map<String,Object> map;
    private HashMap<String, String> qqAndPwd = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        setContentView(R.layout.activity_login);
        Config.setNotificationBar(this,R.color.colorLogin);
        initView();
        initPopWindow();
        bundle = getIntent().getExtras();
        if(bundle!=null){
            edtPassword.setText("");
            imgHead.setImageResource(R.drawable.qq_icon);
            edtQQnumber.setText(bundle.getString(Constant.KEY_QQNUMBER));
        }

        RongIM.setUserInfoProvider(this,true);
        initClick();
    }



    private void initPopWindow() {
        db = new DBLocal(this);
        SQLiteDatabase read = db.getReadableDatabase();
        String qqnumber;
        String password;
        Cursor c = read.query("record", null, null, null, null, null, null);
        while (c.moveToNext()) {
            qqnumber = c.getString(c.getColumnIndex("qqnumber"));
            password = c.getString(c.getColumnIndex("password"));
            image = c.getString(c.getColumnIndex("image"));
            map = new HashMap<>();
            map.put(Constant.KEY_QQNUMBER, qqnumber);
            map.put(Constant.KEY_IMAGE, image);
            map.put(Constant.KEY_DELETE, R.mipmap.arrow_delete);
            popList.add(map);
            qqAndPwd.put(qqnumber, password);
        }
        read.close();
        c.close();
        adapter = new UserRecordAdapter(this, popList);
        popLayout = LayoutInflater.from(this).inflate(R.layout.pop_user_record, null);
        popRecord = new PopupWindow(popLayout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popRecord.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popRecord.setFocusable(true);
        lvPop = (ListView) popLayout.findViewById(R.id.list_user_record);
        lvPop.setAdapter(adapter);
        adapter.setUserRecordListener(this);
    }

    private void initClick() {
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Regist.class));
                overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

        imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popRecord.showAsDropDown(edtQQnumber);
                if (imgExpand
                        .getDrawable()
                        .getConstantState()
                        .equals(getResources().getDrawable(R.mipmap.arrow_down)
                                .getConstantState())) {
                    imgExpand.setImageResource(R.mipmap.arrow_up);
                }
            }
        });

        popRecord.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imgExpand.setImageResource(R.mipmap.arrow_down);
            }
        });

        lvPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item = (HashMap) parent.getItemAtPosition(position);
                String qqnumber = String.valueOf(item.get(
                        Constant.KEY_QQNUMBER).toString());
                String image = String.valueOf(item.get(
                        Constant.KEY_IMAGE).toString());
                edtQQnumber.setText(qqnumber);
                edtPassword.setText(qqAndPwd.get(qqnumber));
                imgHead.setImageBitmap(ImageUtils.stringToBitmap(image));
                popRecord.dismiss();
            }
        });

        edtQQnumber.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtPassword.setText("");
                    imgHead.setImageResource(R.drawable.qq_icon);
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("正在验证个人信息，请稍后");
                dialog.show();
                if(friendsListAgreed.size()!=0){
                    friendsListAgreed.clear();
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
                            final Editor editor = sp.edit();
                            editor.putString(Constant.KEY_QQNUMBER, edtQQnumber.getText().toString());
                            editor.putString(Constant.KEY_PASSWORD, edtPassword.getText().toString());

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

                                    //将图片缓存到本地
                                    final Handler handler = new Handler(){
                                        @Override
                                        public void handleMessage(Message msg) {
                                            if(msg.what == 0){
                                                String imageBase64 = ImageUtils.bitmapToString(localBm);
                                                editor.putString(Constant.KEY_IMAGE,imageBase64);
                                                editor.apply();
                                                recordUser(qqnumber,edtPassword.getText().toString(),imageBase64);
                                            }
                                        }
                                    };

                                    new Thread(){
                                        @Override
                                        public void run() {
                                            localBm = ImageUtils.receiveImage(image);
                                            Message msg = new Message();
                                            msg.what = 0;
                                            handler.sendMessage(msg);
                                        }
                                    }.start();


                                    //----------------------------------------------------

                                    user = new User(qqnumber,nickname,truename,sex,age,phone,token,signature,image);
                                    App.user = user;
                                    RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                        @Override
                                        public void onTokenIncorrect() {
                                            dialog.dismiss();
                                            Toast.makeText(Login.this,"token错误请稍后重试",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSuccess(String s) {
                                            int status;
                                            for (int j = 1;j<array.length();j++){
                                                JSONObject friendsObject ;
                                                try {
                                                    friendsObject = array.getJSONObject(j);
                                                    /**
                                                     * 添加好友状态status说明：
                                                     *      0:不是好友
                                                     *      1:已经是好友
                                                     *      2:请求者
                                                     *      3:被请求者
                                                     */
                                                    status = friendsObject.getInt(Constant.KEY_FRIENDS_STATUS);
                                                    if(status == 1){
                                                        friends = new Friends(friendsObject.getString(Constant.KEY_FRIENDS_QQNUMBER),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_NICK),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_SEX),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_PHONE),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_TOKEN),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_IMAGE),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_SIGNATURE));
                                                        friendsListAgreed.add(friends);
                                                    }else if(status == 3){
                                                        friendInPanding = new ConfirmFriendBean(friendsObject.getString(Constant.KEY_FRIENDS_QQNUMBER),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_NICK),
                                                                friendsObject.getString(Constant.KEY_FRIENDS_IMAGE),
                                                                friendsObject.getString(Constant.KEY_APPLY_MESSAGE));
                                                        friendsListInPanding.add(friendInPanding);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            App.friendsListAgreed = friendsListAgreed;
                                            App.friendsListInPending = friendsListInPanding;
                                            Intent intent = new Intent(Login.this,MainUI.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(Constant.KEY_PASSWORD,edtPassword.getText().toString());
                                            bundle.putSerializable(Constant.KEY_USER,user);
                                            if(friendsListAgreed.size()!=0){
                                                bundle.putSerializable(Constant.KEY_FRIENDS, friendsListAgreed);
                                            }
                                            intent.putExtras(bundle);
                                            dialog.dismiss();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                                        }
                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            dialog.dismiss();
                                            MyToast.showToast(Login.this, "外部网络错误请稍后重试",R.mipmap.error,Toast.LENGTH_SHORT);
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

    private void recordUser(String qqnumber,String password,String image) {
        db = new DBLocal(this);
        if(!db.isExistInRecord(qqnumber)){
            db.insertToRecord(qqnumber,password,image);
        }else{
            db.updateInRecord(qqnumber,password,image);
        }
    }

    private void initView() {
        login = (Button) findViewById(R.id.btn_login_login);
        regist = (Button) findViewById(R.id.btn_login_regist);
        edtPassword = (EditText) findViewById(R.id.edt_login_password);
        edtQQnumber = (EditText) findViewById(R.id.edt_login_qqnumber);
        imgHead = (ImageView) findViewById(R.id.img_login_head);
        imgExpand = (ImageView) findViewById(R.id.img_expand);
        SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
        edtQQnumber.setText(sp.getString(Constant.KEY_QQNUMBER,""));
        edtPassword.setText(sp.getString(Constant.KEY_PASSWORD,""));
        if(!sp.getString(Constant.KEY_IMAGE,"").equals("")){
            imgHead.setImageBitmap(ImageUtils.stringToBitmap(sp.getString(Constant.KEY_IMAGE,"")));
        }
        if(edtQQnumber.getText().toString().equals("")){
            imgExpand.setVisibility(View.GONE);
        }
        ScreenManager screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
        friendsListAgreed = new ArrayList<>();
        friendsListInPanding = new ArrayList<>();
        dialog = new ProgressDialog(this);
    }

    @Override
    public UserInfo getUserInfo(String userId) {

        if(userId.equals("123456")){
            return new UserInfo(userId,"系统消息",null);
        }

        if(friendsListAgreed !=null){
            for(Friends friend : friendsListAgreed){
                if (userId.equals(friend.getFriendQQ())){
                    return new UserInfo(userId,friend.getFriendNick(),Uri.parse(friend.getFriendImg()));
                }
            }
        }
        if(user!=null){
            if(userId.equals(user.getQQnumber())){
                return new UserInfo(userId,user.getNickname(),Uri.parse(user.getImage()));
            }
        }
        return null;
    }

    @Override
    public void click(View v, final int position) {
        switch (v.getId()){
            case R.id.img_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("系统提示");
                builder.setMessage("确认删除该账号的登录记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String qqnumber = (String) popList.get(position).get(Constant.KEY_QQNUMBER);
                        if (qqnumber.equals(edtQQnumber.getText().toString())) {
                            edtPassword.setText("");
                            edtQQnumber.setText("");
                            imgHead.setImageResource(R.drawable.qq_icon);
                        }
                        db = new DBLocal(Login.this);
                        db.deleteFromRecord(qqnumber);
                        popList.remove(position);
                        adapter.notifyDataSetChanged();
                        popRecord.dismiss();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                break;
        }
    }
}
