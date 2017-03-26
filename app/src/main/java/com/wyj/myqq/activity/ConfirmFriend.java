package com.wyj.myqq.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.wyj.myqq.adapter.ConfirmFriendAdapter;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmFriend extends AppCompatActivity implements View.OnClickListener,ConfirmFriendAdapter.OnConfirmFriendListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title,toast;
    private ListView listFriend;
    private Bundle bundle;
    private ArrayList<String> listSourceId,listApplyMessage;

    private String qqnumber,friendNick,friendImg;
    private Bitmap friendBm;
    private ConfirmFriendBean friendBean;
    private ArrayList<ConfirmFriendBean> list;
    private ConfirmFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_friend);
        initView();
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        listSourceId = bundle.getStringArrayList(Constant.KEY_SET_SOURCEID);
        listApplyMessage = bundle.getStringArrayList(Constant.KEY_SET_MESSAGE);
        if(listSourceId.size() == 0){
            toast.setVisibility(View.VISIBLE);
        }
        for(int j = 0;j<listSourceId.size();j++){
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add(Constant.KEY_QQNUMBER,listSourceId.get(j));
            final int finalJ = j;
            client.post(Constant.HTTP_SEARCH_FRIEND, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String result = new String(bytes);
                    try {
                        Log.d("ConfirmFriend","run here");
                        JSONObject object = new JSONObject(result);
                        friendNick = object.getString(Constant.KEY_NICK);
                        friendImg = object.getString(Constant.KEY_IMAGE);
                        friendBean = new ConfirmFriendBean(listSourceId.get(finalJ),friendNick,friendImg,listApplyMessage.get(finalJ));
                        list.add(friendBean);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(ConfirmFriend.this,"内部网络服务器错误请稍后重试",Toast.LENGTH_SHORT).show();
                }

            });
        }

        adapter = new ConfirmFriendAdapter(this,list);
        listFriend.setAdapter(adapter);
        Log.d("ConfirmFriendhoumian","run here");
        adapter.setOnConfirmListener(this);
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
    public void click(View v, int position) {
        switch (v.getId()){
            case R.id.btn_accept:
                adapter.setAccept(true);
                adapter.notifyDataSetChanged();
                submitApply(Constant.KEY_ACCEPT,position);
                break;
            case R.id.btn_refuse:
                adapter.setRefuse(true);
                adapter.notifyDataSetChanged();
               // submitApply(Constant.KEY_REFUSE,position);
                break;
        }
    }

    private void submitApply(String result, final int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER,qqnumber);
        params.add(Constant.KEY_FRIENDS_QQNUMBER,listSourceId.get(position));
        params.add(Constant.KEY_ADDFRIEND_RESULT,result);
        client.post(Constant.HTTP_RESULT_ADDFRIEND, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);

                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getInt("success") == 0){
                        Friends friends = new Friends(listSourceId.get(position),friendNick,
                                object.getString(Constant.KEY_FRIENDS_SEX),
                                object.getString(Constant.KEY_FRIENDS_PHONE),
                                object.getString(Constant.KEY_FRIENDS_TOKEN),
                                friendImg,object.getString(Constant.KEY_FRIENDS_SIGNATURE));
                        Intent intent = new Intent();
                        intent.putExtra(Constant.KEY_FRIENDS,friends);
                        setResult(Constant.RESULT_CODE_CONFIRM_FRIEND,intent);
                        finish();
                    }else{
                        Toast.makeText(ConfirmFriend.this,"外部网络服务器错误请稍后重试",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(ConfirmFriend.this,"内部网络服务器错误请稍后重试",Toast.LENGTH_SHORT).show();
            }

        });
    }
}
