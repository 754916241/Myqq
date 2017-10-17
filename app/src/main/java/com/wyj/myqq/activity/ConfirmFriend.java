package com.wyj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.adapter.ConfirmFriendAdapter;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmFriend extends AppCompatActivity implements View.OnClickListener, ConfirmFriendAdapter.OnConfirmFriendListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title, toast;
    private ListView listFriend;
    private Bundle bundle;
    private String qqnumber;
    private ArrayList<ConfirmFriendBean> friendsListInPending,friendsListAgreed;
    private ArrayList<Friends> agreeFriends;
    private ConfirmFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_friend);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
        initData();

    }

    private void initData() {
        friendsListInPending = App.friendsListInPending;
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        friendsListAgreed = new ArrayList<>();
        if (friendsListInPending == null) {
            toast.setVisibility(View.VISIBLE);
        }else{
            agreeFriends = new ArrayList<>();
            adapter = new ConfirmFriendAdapter(ConfirmFriend.this, friendsListInPending);
            listFriend.setAdapter(adapter);
            adapter.setOnConfirmListener(ConfirmFriend.this);
        }
    }


    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        listFriend = (ListView) findViewById(R.id.list_friend);
        toast = (TextView) findViewById(R.id.tv_toast);
        title.setText("好友验证");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /**
         * 如果用户进入页面并同意申请，则执行if语句
         */
        if(agreeFriends!=null && agreeFriends.size()!=0){
            App.friendsListInPending.removeAll(friendsListAgreed);
            Intent intent = new Intent();
            intent.putExtra(Constant.KEY_FRIENDS, agreeFriends);
            setResult(Constant.RESULT_CODE_CONFIRM_FRIEND, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void click(View v, int position) {
        switch (v.getId()) {
            case R.id.btn_accept:
                adapter.setAccept(true);
                adapter.notifyDataSetChanged();
                submitApply(Constant.KEY_ACCEPT, position);

                break;
            //拒绝按钮
            /*case R.id.btn_refuse:
                adapter.setRefuse(true);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                listRefuse.add(listSourceId.get(position));
                intent.putExtra(Constant.KEY_FRIENDS, listRefuse);
                setResult(Constant.RESULT_CODE_CONFIRM_FRIEND_REFUSE, intent);
                // submitApply(Constant.KEY_REFUSE,position);
                break;*/
        }
    }

    private void submitApply(String result, final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        params.add(Constant.KEY_FRIENDS_QQNUMBER, friendsListInPending.get(position).getFriendQQ());
        params.add(Constant.KEY_ADDFRIEND_RESULT, result);
        // TODO: 2017/10/17  服务器端应该在此时改变好友状态
        client.post(Constant.HTTPURL_RESULT_ADDFRIEND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("success") == 0) {
                        Friends friend = new Friends(friendsListInPending.get(position).getFriendQQ(),
                                friendsListInPending.get(position).getFriendNick(),
                                object.getString(Constant.KEY_FRIENDS_SEX),
                                object.getString(Constant.KEY_FRIENDS_PHONE),
                                object.getString(Constant.KEY_FRIENDS_TOKEN),
                                friendsListInPending.get(position).getFriendImg(),
                                object.getString(Constant.KEY_FRIENDS_SIGNATURE));
                        agreeFriends.add(friend);
                        friendsListAgreed.add(friendsListInPending.get(position));
                    } else {
                        MyToast.showToast(ConfirmFriend.this, "外部网络服务器错误请稍后重试", Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ConfirmFriend.this, "内部网络服务器错误请稍后重试", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
/**
 *服务器需要
 1.更改friends表，加一个status状态，并在login的时候返回这个状态(修改数据库语句以查询)
 2.添加一个friendstatus字段
 3.验证完成后改变好友状态
 */