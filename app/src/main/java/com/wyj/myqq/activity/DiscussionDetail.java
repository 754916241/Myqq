package com.wyj.myqq.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.Conversation;
import com.wyj.myqq.adapter.ConfirmFriendAdapter;
import com.wyj.myqq.adapter.DiscussionMembersAdapter;
import com.wyj.myqq.bean.ConfirmFriendBean;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ScreenManager;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Discussion;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CHANGE_DISCUSSION_NAME;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CHANGE_DISCUSSION_NAME;

public class DiscussionDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;
    private TextView title;

    private TextView tvDiscussionName,tvDiscussionMembers;
    private RelativeLayout layoutDiscussionName;
    private GridView gvDiscussionMembers;
    /**
     * 退出讨论组
     */
    private Button btnExitDiscussion;
    private String targetId,name;
    private ArrayList<String> membersId;
    private String membersName,membersImg,membersSex,membersPhone,membersSignature,membersToken;
    private ConfirmFriendBean membersBean;
    private Friends membersDetailBean;
    private ArrayList<ConfirmFriendBean> list;
    private ArrayList<Friends> listDetail;
    private DiscussionMembersAdapter adapter;
    private ScreenManager screenManager;
    private String qqnumber;
    private Discussion discussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_detail);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
        initData();
        gvDiscussionMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //MyToast.showToast(DiscussionDetail.this, position+"",  Toast.LENGTH_SHORT);
                Intent intent;
                if(position == list.size()-1){
                    intent = new Intent(DiscussionDetail.this,CreateDiscussGroup.class);
                    //将已经在讨论组中的好友传过去
                    intent.putExtra(Constant.KEY_FRIENDS,listDetail);
                    //讨论组id传过去
                    intent.putExtra(Constant.KEY_DISCUSSION_ID,targetId);
                    startActivityForResult(intent,Constant.REQUEST_CODE_ADD_DISCUSSION_MEMBERS);
                }else{
                    if(!listDetail.get(position).getFriendQQ().equals(App.user.getQQnumber())){
                        intent = new Intent(DiscussionDetail.this,FriendDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_QQNUMBER,qqnumber);
                        bundle.putSerializable(Constant.KEY_FRIENDS,listDetail.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }



    private void initData() {
        title.setText("聊天设置");
        targetId = getIntent().getStringExtra(Constant.KEY_DISCUSSION_ID);
        list = new ArrayList<>();
        listDetail = new ArrayList<>();
        screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
        RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() {
            @Override
            public void onSuccess(Discussion discussion) {
                name = discussion.getName();
                DiscussionDetail.this.discussion = discussion;
                tvDiscussionName.setText(name);
                membersId = (ArrayList<String>) discussion.getMemberIdList();
                for(String id : membersId){
                    Log.d("membersId",id);
                }
                tvDiscussionMembers.setText(tvDiscussionMembers.getText()+"("+membersId.size()+"人)");
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put(Constant.KEY_QQNUMBER, membersId);
                client.post(Constant.HTTP_CONFIRM_FRIEND, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        String result = new String(bytes);
                        try {
                            JSONArray array = new JSONArray(result);
                            JSONObject object;
                            for(int j = 0;j<array.length();j++){
                                object = array.getJSONObject(j);
                                membersName = object.getString(Constant.KEY_NICK);
                                membersImg = object.getString(Constant.KEY_IMAGE);
                                membersPhone = object.getString(Constant.KEY_PHONE);
                                membersSex = object.getString(Constant.KEY_SEX);
                                membersToken = object.getString(Constant.KEY_TOKEN);
                                membersSignature = object.getString(Constant.KEY_SIGNATURE);
                                membersDetailBean = new Friends(membersId.get(j),membersName,
                                        membersSex,membersPhone,membersToken,membersImg,membersSignature);
                                membersBean = new ConfirmFriendBean(membersId.get(j), membersName, membersImg);
                                list.add(membersBean);
                                listDetail.add(membersDetailBean);
                            }
                            qqnumber = App.user.getQQnumber();
                            membersBean = new ConfirmFriendBean(qqnumber,"","");
                            list.add(membersBean);
                            adapter = new DiscussionMembersAdapter(DiscussionDetail.this, list);
                            gvDiscussionMembers.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        MyToast.showToast(DiscussionDetail.this, "内部网络错误请稍后重试", R.mipmap.error, Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MyToast.showToast(DiscussionDetail.this, "外部网络错误，错误码为"+errorCode, R.mipmap.error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        tvDiscussionName = (TextView) findViewById(R.id.tv_discussion_name);
        tvDiscussionMembers = (TextView) findViewById(R.id.tv_discussion_members);
        layoutDiscussionName = (RelativeLayout) findViewById(R.id.layout_discussion_name);
        layoutDiscussionName.setOnClickListener(this);
        gvDiscussionMembers = (GridView) findViewById(R.id.gv_discussion_members);
        btnExitDiscussion = (Button) findViewById(R.id.btn_exit_discussion);
        btnExitDiscussion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                onBackPressed();
                break;
            case R.id.layout_discussion_name:
                Intent intent = new Intent(this,ChangeMyData.class);
                intent.putExtra(Constant.KEY_DISCUSSION_NAME,name);
                startActivityForResult(intent,REQUEST_CODE_CHANGE_DISCUSSION_NAME);
                break;
            case R.id.btn_exit_discussion:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("系统提示");
                builder.setMessage("确认退出该讨论组?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        membersId.remove(qqnumber);
                        discussion.setMemberIdList(membersId);
                        RongIM.getInstance().quitDiscussion(targetId, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                MyToast.showToast(DiscussionDetail.this, "已退出讨论组", Toast.LENGTH_SHORT);
                                RongIM.getInstance().removeConversation(
                                        io.rong.imlib.model.Conversation.ConversationType.DISCUSSION,targetId,null);
                                screenManager.popAllActivityExceptOne(Conversation.class);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });


                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHANGE_DISCUSSION_NAME){
            if(resultCode == RESULT_CODE_CHANGE_DISCUSSION_NAME){
                RongIM.getInstance().setDiscussionName(targetId, data.getExtras().getString(Constant.KEY_DISCUSSION_NAME), new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        tvDiscussionName.setText(data.getExtras().getString(Constant.KEY_DISCUSSION_NAME));
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        MyToast.showToast(DiscussionDetail.this, "外部网络错误，错误码为"+errorCode, R.mipmap.error, Toast.LENGTH_SHORT);
                    }
                });
            }

        }
    }



}
