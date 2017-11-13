package com.wyj.myqq;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.activity.DiscussionDetail;
import com.wyj.myqq.activity.SendLocation;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ScreenManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.example.wyj.myqq.App.user;

public class Conversation extends FragmentActivity implements View.OnClickListener,RongIM.LocationProvider {

    private ImageView imgLeft;
    private TextView title;
    private ImageView imgRight;
    private String sTitle, targetId;
    private InputMethodManager imm;
    private io.rong.imlib.model.Conversation.ConversationType conversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener(){

            @Override
            public void onTypingStatusChanged(io.rong.imlib.model.Conversation.ConversationType type, String s, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(conversationType) && s.equals(targetId)) {
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    int count = typingStatusSet.size();
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        // TODO: 2017/10/11 是否在网络中还需测试  
                        if (objectName.equals(textTag.value())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    title.setText("对方正在输入");
                                }
                            });
                        } else if (objectName.equals(voiceTag.value())) {
                           title.setText("对方正在讲话");
                        }
                    } else {
                        title.setText(targetId);
                    }
                }
            }
        });
    }

    private void initData() {

        //目标Id
        targetId = getIntent().getData().getQueryParameter("targetId");
        sTitle = getIntent().getData().getQueryParameter("title");

        conversationType = io.rong.imlib.model.Conversation.ConversationType.valueOf(getIntent().getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));
        if(conversationType.equals(io.rong.imlib.model.Conversation.ConversationType.DISCUSSION)){
            imgRight.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(sTitle)){
            title.setText(sTitle);
        }else{
            title.setText(targetId);
        }
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getQQnumber()
                ,user.getNickname(), Uri.parse(user.getImage())));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScreenManager screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        imgRight = (ImageView) findViewById(R.id.img_right);
        imgRight.setOnClickListener(this);
        imgLeft.setVisibility(View.VISIBLE);
        RongIM.setLocationProvider(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                imm.hideSoftInputFromWindow(findViewById(android.R.id.edit).getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.img_right:
                if(conversationType.equals(io.rong.imlib.model.Conversation.ConversationType.DISCUSSION)){
                    Intent intent = new Intent(this, DiscussionDetail.class);
                    intent.putExtra(Constant.KEY_DISCUSSION_ID,targetId);
                    intent.putExtra(Constant.KEY_DISCUSSION_NAME,sTitle);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        Intent intent = new Intent(context,SendLocation.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_CONVERSATION_TYPE,conversationType);
        bundle.putString(Constant.KEY_FRIENDS_QQNUMBER,targetId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
