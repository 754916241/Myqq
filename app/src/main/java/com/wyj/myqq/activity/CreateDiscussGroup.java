package com.wyj.myqq.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.example.wyj.myqq.R;
import com.wyj.myqq.adapter.CreateDiscussAdapter;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class CreateDiscussGroup extends AppCompatActivity implements View.OnClickListener {

    /**
     * 取消
     */
    private TextView tvLeft;
    private TextView tvTitle;

    private TextView tvRight;
    private ListView lvChooseFriend;

    /**
     * 从MainUI那里跳转过来所传递的数据
     */
    private Bundle bundle;
    private String qqnumber, nickname;
    private ArrayList<Friends> listFriends;
    private CreateDiscussAdapter adapter;
    private ArrayList<String> targetUserIds;

    /**
     * 从DisscussionDetail跳转过来所传递的数据
     */
    private ArrayList<Friends> existFriend;
    private ArrayList<Friends> allFriends;
    private String targetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_discuss_group);
        Config.setNotificationBar(this,R.color.colorApp);
        initData();
        initView();

    }

    private void initData() {
        if(getIntent().getStringExtra(Constant.KEY_DISCUSSION_ID)==null){
            bundle = getIntent().getExtras();
            qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
            nickname = bundle.getString(Constant.KEY_NICK);
            listFriends = (ArrayList<Friends>) bundle.getSerializable(Constant.KEY_FRIENDS);
        }else{
            allFriends = new ArrayList<>();
            existFriend = (ArrayList<Friends>) getIntent().getSerializableExtra(Constant.KEY_FRIENDS);
            targetId = getIntent().getStringExtra(Constant.KEY_DISCUSSION_ID);
        }
        targetUserIds = new ArrayList<>();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        lvChooseFriend = (ListView) findViewById(R.id.lv_choose_friend);
        tvTitle.setText("选择联系人");
        tvRight.setText("完成");
        if (listFriends != null) {
            //创建讨论组适配器
            adapter = new CreateDiscussAdapter(this, listFriends);

            lvChooseFriend.setAdapter(adapter);
        } else {
            //添加讨论组成员适配器
            allFriends = App.friendsListAgreed;
            allFriends.removeAll(existFriend);
            adapter = new CreateDiscussAdapter(this, allFriends);
            lvChooseFriend.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                if (adapter.getChooseList().size() == 0) {
                    MyToast.showToast(CreateDiscussGroup.this, "请选择好友", Toast.LENGTH_SHORT);
                } else {
                    if (listFriends != null) {
                        //创建讨论组适配器
                        startDiscussGroup();
                    } else {
                        addMembersToDiscussGroup();
                    }

                }
                break;
        }
    }

    private void addMembersToDiscussGroup() {
        for (Friends friends : adapter.getChooseList()) {
            targetUserIds.add(friends.getFriendQQ());
        }
        RongIM.getInstance().addMemberToDiscussion(targetId, targetUserIds, null);
        finish();
    }

    private void startDiscussGroup() {
        targetUserIds.add(qqnumber);
        String title = "";
        for (Friends friends : adapter.getChooseList()) {
            targetUserIds.add(friends.getFriendQQ());
            title = nickname + "," + title + friends.getFriendNick() + ",";
        }
        title = title.substring(0, title.length() - 1);
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().createDiscussionChat(this, targetUserIds, title, new RongIMClient.CreateDiscussionCallback() {
                @Override
                public void onSuccess(String s) {
                    TextMessage textMessage = TextMessage.obtain("我已创建了讨论组");
                    Message message = Message.obtain(s, Conversation.ConversationType.DISCUSSION, textMessage);
                    RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
        finish();

    }
}
