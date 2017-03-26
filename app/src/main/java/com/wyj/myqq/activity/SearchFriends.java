package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.adapter.SearchFriendResultAdapter;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ScreenManager;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFriends extends AppCompatActivity implements TextWatcher {

    private EditText edtSearchFriend;
    private TextView tvSearch,tvCancel;
    private String qqnumber;
    private int success;
    private Friends friends;
    private InputMethodManager imm;
    private ListView lvResult;
    private ArrayList<Friends> list;
    private SearchFriendResultAdapter adapter;
    private ScreenManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        Bundle bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        list = new ArrayList<>();
        manager = ScreenManager.getScreenManager();
        manager.pushActivity(this);
        init();
    }

    private void init() {

        edtSearchFriend = (EditText) findViewById(R.id.edt_search_friend);
        tvCancel = (TextView) findViewById(R.id.tv_left);
        tvSearch = (TextView) findViewById(R.id.tv_right);
        lvResult = (ListView) findViewById(R.id.lv_search_result);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qqnumber.equals(edtSearchFriend.getText().toString())) {
                    MyToast.showToast(SearchFriends.this, "不能添加自己为好友", Toast.LENGTH_SHORT);
                }else{
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.add(Constant.KEY_QQNUMBER, edtSearchFriend.getText().toString());
                    client.post(Constant.HTTP_SEARCH_FRIEND, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            String response = new String(bytes);
                            try {
                                JSONObject object = new JSONObject(response);
                                if(object.getInt("success")==0){
                                    String nickname = object.getString(Constant.KEY_NICK);
                                    String imgPath = object.getString(Constant.KEY_IMAGE);
                                    friends = new Friends(edtSearchFriend.getText().toString(),nickname,imgPath);
                                    list.add(friends);
                                    adapter = new SearchFriendResultAdapter(SearchFriends.this,list);
                                    lvResult.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }else if(object.getInt("success")==1){
                                    MyToast.showToast(SearchFriends.this,"您搜索的用户不存在，请核对后重试",Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            MyToast.showToast(SearchFriends.this,"内网错误请稍后重试",Toast.LENGTH_SHORT);
                        }
                    });
                }
               // addFriends();
                lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SearchFriends.this,AddFriends.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.KEY_FRIENDS,friends);
                        bundle.putString(Constant.KEY_QQNUMBER,qqnumber);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });



        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(edtSearchFriend.getWindowToken(), 0);
                onBackPressed();
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(list.size()!=0){
            list.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
