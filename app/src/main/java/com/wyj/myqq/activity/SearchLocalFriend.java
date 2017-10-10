package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.wyj.myqq.adapter.SearchFriendResultAdapter;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

import java.util.ArrayList;

public class SearchLocalFriend extends AppCompatActivity {

    private SearchView mSvLocal;
    /**
     * 取消
     */
    private TextView mTvCancel;
    private ListView mLvResult;
    private InputMethodManager imm;
    private SearchFriendResultAdapter adapter;
    private Bundle bundle;
    private ArrayList<Friends> allFriends,inputFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_local_friend);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
        initData();
        initClick();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        allFriends = (ArrayList<Friends>) bundle.getSerializable(Constant.KEY_FRIENDS);
        if(allFriends == null){
            allFriends = new ArrayList<>();
        }
        inputFriend = new ArrayList<>();
    }

    private void initClick() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(mSvLocal.getWindowToken(), 0);
                onBackPressed();
            }
        });

        mSvLocal.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for(Friends friend : allFriends){
                    if(friend.getFriendQQ().contains(query)||friend.getFriendNick().contains(query)){
                        inputFriend.add(friend);
                    }
                }
                if(inputFriend.size() == 0){
                    MyToast.showToast(SearchLocalFriend.this,"该好友不存在于您的好友列表中",Toast.LENGTH_SHORT);
                }else{
                    adapter = new SearchFriendResultAdapter(SearchLocalFriend.this,inputFriend);
                    mLvResult.setAdapter(adapter);
                }
                imm.hideSoftInputFromWindow(mSvLocal.getWindowToken(), 0);
                mSvLocal.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()){
                    inputFriend.clear();
                    adapter = new SearchFriendResultAdapter(SearchLocalFriend.this,inputFriend);
                    mLvResult.setAdapter(adapter);
                }
                return false;
            }
        });

        mLvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchLocalFriend.this,FriendDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_FRIENDS,inputFriend.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initView() {
        mSvLocal = (SearchView) findViewById(R.id.tv_search);
        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mLvResult = (ListView) findViewById(R.id.lv_result);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
