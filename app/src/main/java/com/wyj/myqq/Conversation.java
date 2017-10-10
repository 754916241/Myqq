package com.wyj.myqq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Locale;

import io.rong.imkit.RongIM;

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

    }

    private void initData() {

        //目标Id
        targetId = getIntent().getData().getQueryParameter("targetId");
        sTitle = getIntent().getData().getQueryParameter("title");
        //getIntent().getData().
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
