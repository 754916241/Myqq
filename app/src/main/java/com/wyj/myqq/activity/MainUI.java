package com.wyj.myqq.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.App;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.fragment.Contacts;
import com.wyj.myqq.fragment.Setting;
import com.wyj.myqq.utils.ImageUtils;
import com.wyj.myqq.view.MyToast;
import com.wyj.myqq.utils.ScreenManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.TextMessage;

import static android.os.Build.VERSION_CODES.M;
import static com.example.wyj.myqq.R.id.img;
import static com.wyj.myqq.utils.Constant.KEY_AGE;
import static com.wyj.myqq.utils.Constant.KEY_NICK;
import static com.wyj.myqq.utils.Constant.KEY_QQNUMBER;
import static com.wyj.myqq.utils.Constant.KEY_SEX;
import static com.wyj.myqq.utils.Constant.KEY_SIGNATURE;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CHANGEAGE;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CHANGENICK;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CHANGESEX;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CHANGESIGNATURE;
import static com.wyj.myqq.utils.Constant.REQUEST_CODE_CONFIRM_FRIEND;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CHANGEAGE;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CHANGENICK;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CHANGESEX;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CHANGESIGNATURE;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CONFIRM_FRIEND;
import static com.wyj.myqq.utils.Constant.RESULT_CODE_CONFIRM_FRIEND_REFUSE;
import static io.rong.imkit.common.RongConst.EXTRA.RES;


public class MainUI extends FragmentActivity implements Setting.OnSettingListener,
        Contacts.OnContactsListener, RongIMClient.OnReceiveMessageListener, RongIM.ConversationListBehaviorListener{


    private Bundle bundle;
    private ArrayList<Friends> listFriends = null;
    private User user;
    private ImageView imgMessage, imgContacts, imgSetting;
    private TextView tvMessage, tvContacts, tvSetting;
    private FragmentManager manager;
    private Contacts contacts;
    private Setting setting;
    private ConversationListFragment message;
    private View messageLayout, contactsLayout, settingLayout;
    private String password;
    private TextView title;
    private ScreenManager screenManager;
    private ProgressDialog dialog;
    private SharedPreferences sp;
    private HashSet<String> setSourceId, setMessage;
    private ArrayList<String> listSourceId, listMessage;
    private HashMap<String, String> map;
    private String imageUriString = "",imageBase64 = "";
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainui);
        Config.setNotificationBar(this,R.color.colorApp);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            listFriends = (ArrayList<Friends>) bundle.getSerializable(Constant.KEY_FRIENDS);
            user = (User) bundle.getSerializable(Constant.KEY_USER);
            password = bundle.getString(Constant.KEY_PASSWORD);
        }
        initSet();
        init();
        setTabSelection(0);
    }

    private void initSet() {
        sp = getSharedPreferences(
                "mysp", Context.MODE_PRIVATE);
        map = new HashMap<>();
        setSourceId = (HashSet<String>) sp.getStringSet(Constant.KEY_SET_SOURCEID, null);
        setMessage = (HashSet<String>) sp.getStringSet(Constant.KEY_SET_MESSAGE, null);
        if (setSourceId != null) {
            Iterator<String> itSourceId = setSourceId.iterator();
            Iterator<String> itMessage = setMessage.iterator();
            while (itSourceId.hasNext()) {
                map.put(itSourceId.next(), itMessage.next());
            }
            if (listFriends != null) {
                Iterator<Friends> itFriend = listFriends.iterator();

                while (itFriend.hasNext()) {
                    String friendQQ = itFriend.next().getFriendQQ();
                    map.remove(friendQQ);
                }
            }
            listSourceId = new ArrayList<>();
            listSourceId.addAll(map.keySet());
            if(listSourceId.size() == 0){
                RongIM.getInstance().getRongIMClient().removeConversation(
                        Conversation.ConversationType.SYSTEM,"123456");
            }
            listMessage = new ArrayList<>();
            listMessage.addAll(map.values());
        }
    }

    private void init() {
        imgContacts = (ImageView) findViewById(R.id.img_mainUI_contacts);
        imgMessage = (ImageView) findViewById(R.id.img_mainUI_message);
        imgSetting = (ImageView) findViewById(R.id.img_mainUI_setting);
        tvContacts = (TextView) findViewById(R.id.tv_mainUI_contacts);
        tvMessage = (TextView) findViewById(R.id.tv_mainUI_message);
        tvSetting = (TextView) findViewById(R.id.tv_mainUI_settting);
        title = (TextView) findViewById(R.id.title);
        dialog = new ProgressDialog(this);
        screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
        manager = getSupportFragmentManager();
        messageLayout = findViewById(R.id.message_layout);
        contactsLayout = findViewById(R.id.contacts_layout);
        settingLayout = findViewById(R.id.setting_layout);

        initRong();
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.message_layout:
                        setTabSelection(0);
                        break;
                    case R.id.contacts_layout:
                        setTabSelection(1);
                        break;
                    case R.id.setting_layout:
                        setTabSelection(2);
                        break;
                }
            }
        };
        messageLayout.setOnClickListener(l);
        contactsLayout.setOnClickListener(l);
        settingLayout.setOnClickListener(l);

    }

    private void initRong() {
        RongIM.setOnReceiveMessageListener(this);
        RongIM.setConversationListBehaviorListener(this);
    }

    private void setTabSelection(int i) {
        clearSelection();
        // 开启一个fragment任务
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                tvMessage.setTextColor(Color.parseColor("#4396FF"));   // 当点击消息tab时，改变控件的图片和文字颜色
                imgMessage.setImageResource(R.mipmap.tab_message_pressed);
                title.setText("消息");
                // 如果messageFragment为空，则创建一个添加到界面上
                if (message == null) {
                    message = ConversationListFragment.getInstance();
                    Uri uri = Uri.parse("rong://" + this.getApplicationInfo().packageName).buildUpon()
                            .appendPath("conversationlist")
                            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                            .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                            .build();
                    message.setUri(uri);
                    transaction.add(R.id.fragment_content, message);
                }
                transaction.show(message);

                break;
            case 1:
                // 当点击联系人tab时，改变控件的图片和文字颜色
                tvContacts.setTextColor(Color.parseColor("#4396FF"));
                imgContacts.setImageResource(R.mipmap.tab_contacts_pressed);
                title.setText("联系人");
                // 如果contactsFragment为空，则创建一个添加到界面上
                if (contacts == null || contactsState == 1) {
                    contacts = new Contacts();
                    contacts.setOnContactsListener(this);
                    Bundle bundle = new Bundle();

                    bundle.putString(Constant.KEY_QQNUMBER,user.getQQnumber());
                    if (listFriends != null) {
                        //Log.e("In contacts listFriends size is",""+listFriends.size());
                        bundle.putSerializable(Constant.KEY_FRIENDS, listFriends);
                        contacts.setArguments(bundle);
                    }
                    transaction.add(R.id.fragment_content, contacts);
                }

                transaction.show(contacts);

                break;
            case 2:
                // 当点击动态tab时，改变控件的图片和文字颜色
                tvSetting.setTextColor(Color.parseColor("#4396FF"));
                imgSetting.setImageResource(R.mipmap.tab_settings_pressed);
                title.setText("个人");
                // 如果newsFragment为空，则创建一个添加到界面上
                if (setting == null || settingState == 1) {
                    setting = new Setting();
                    setting.setOnSettingListener(this);
                    Bundle bundle = new Bundle();
                    if(!imageUriString.equals("")){
                        bundle.putString(Constant.KEY_IMAGE_URI,imageUriString);
                    }
                    if(!imageBase64.equals("")){
                        bundle.putString(Constant.KEY_IMAGE_BASE64,imageBase64);
                    }
                    bundle.putSerializable(Constant.KEY_USER, user);
                    setting.setArguments(bundle);
                    transaction.add(R.id.fragment_content, setting);
                }
                transaction.show(setting);
                break;

        }

        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (message != null) {
            transaction.hide(message);
        }
        if (contacts != null) {
            transaction.hide(contacts);
        }
        if (setting != null) {
            transaction.hide(setting);
        }

    }

    private void clearSelection() {
        tvMessage.setTextColor(Color.GRAY);
        tvContacts.setTextColor(Color.GRAY);
        tvSetting.setTextColor(Color.GRAY);
        imgMessage.setImageResource(R.mipmap.tab_message_normal);
        imgContacts.setImageResource(R.mipmap.tab_contacts_normal);
        imgSetting.setImageResource(R.mipmap.tab_settings_normal);
    }


    private void contactsClickEvent(Class cls,int position){
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        if(position == 0){
            bundle.putString(Constant.KEY_QQNUMBER, user.getQQnumber());
        }else if(position == 1){
            if(listFriends == null){
                MyToast.showToast(this,"您还没有好友，不能创建讨论组",Toast.LENGTH_SHORT);
            }else{
                bundle.putString(Constant.KEY_QQNUMBER, user.getQQnumber());
                bundle.putSerializable(Constant.KEY_FRIENDS,listFriends);
                bundle.putString(Constant.KEY_NICK,user.getNickname());
            }

        }else if(position == 2){
            bundle.putSerializable(Constant.KEY_FRIENDS,listFriends);
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void contactsClick(View view) {
        switch (view.getId()) {
            case R.id.rl_new_friend:
                contactsClickEvent(SearchFriends.class,0);
                break;
            case R.id.rl_group_chat:
                contactsClickEvent(CreateDiscussGroup.class,1);
                break;
            case R.id.tv_search:
                contactsClickEvent(SearchLocalFriend.class,2);
                break;
        }

    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                MyToast.showToast(this, "再按一次退出程序", Toast.LENGTH_SHORT);
                mExitTime = System.currentTimeMillis();
            } else {
                screenManager.popAllActivityExceptOne(Login.class);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void settingClick(View view) {
        switch (view.getId()) {
            case R.id.layout_image:
                String[] items = new String[]{"拍照", "从相册中获取照片","查看大图"};
                new AlertDialog.Builder(this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                switch (arg1) {
                                    case 0:
                                        ImageUtils.pickImageFromcamera(MainUI.this);
                                        break;
                                    case 1:
                                        ImageUtils.pickImageFromalbum(MainUI.this);
                                        break;
                                    case 2:
                                        Intent intent = new Intent(MainUI.this,HeadBigSize.class);
                                        intent.putExtra(Constant.KEY_IMAGE,user.getImage());
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.layout_nick:
                changeData(Constant.KEY_NICK, user.getNickname(), REQUEST_CODE_CHANGENICK);
                break;
            case R.id.layout_sex:
                changeData(KEY_SEX, user.getSex(), REQUEST_CODE_CHANGESEX);
                break;
            case R.id.layout_signature:
                changeData(Constant.KEY_SIGNATURE, user.getSignature(), Constant.REQUEST_CODE_CHANGESIGNATURE);
                break;
            case R.id.layout_age:
                changeData(Constant.KEY_AGE, user.getAge(), Constant.REQUEST_CODE_CHANGEAGE);
                break;
            case R.id.btn_more:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_USER, user);
                bundle.putString(Constant.KEY_PASSWORD, password);
                Intent intent = new Intent(this, MoreSetting.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void changeData(String key, String value, int requestCode) {
        Intent intent = new Intent(MainUI.this, ChangeMyData.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_QQNUMBER, user.getQQnumber());
        bundle.putString(key, value);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }



    /**
     * 调用startActivityForResult()方法当前activity会进入onPause()方法,进入新的activity，
     * 在执行setResult()方法的时候会调用onResume()方法
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_FROM_ALBUM:
                imageUriString = data.getData().toString();
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(50,50));
                imageBase64 = ImageUtils.bitmapToString(ImageUtils.compressBitmap(this,data.getData(),imageView));
                resetSetting();
                postImage();
                break;
            case Constant.REQUEST_CODE_FROM_CAMERA:
                if (!(resultCode == RESULT_CANCELED)) {
                    Bundle bundle = data.getExtras();
                    photo = bundle.getParcelable("data");
                    imageBase64 = ImageUtils.bitmapToString(photo);
                    resetSetting();
                    postImage();
                }
                break;
            case REQUEST_CODE_CHANGENICK:
                if (resultCode == RESULT_CODE_CHANGENICK) {
                    user.setNickname(data.getExtras().getString(KEY_NICK));
                    resetSetting();
                }
                break;
            case REQUEST_CODE_CHANGEAGE:
                if (resultCode == RESULT_CODE_CHANGEAGE) {
                    user.setAge(data.getExtras().getString(KEY_AGE));
                    resetSetting();
                }
                break;
            case REQUEST_CODE_CHANGESIGNATURE:
                if (resultCode == RESULT_CODE_CHANGESIGNATURE) {
                    user.setSignature(data.getExtras().getString(KEY_SIGNATURE));
                    resetSetting();
                }
                break;
            case REQUEST_CODE_CHANGESEX:
                if (resultCode == RESULT_CODE_CHANGESEX) {
                    user.setSex(data.getExtras().getString(KEY_SEX));
                    resetSetting();
                }
                break;
            case REQUEST_CODE_CONFIRM_FRIEND:
                if(resultCode == RESULT_CODE_CONFIRM_FRIEND){
                    if(listFriends == null){
                        listFriends = new ArrayList<>();
                    }
                    ArrayList<Friends> friends = (ArrayList<Friends>) data.getExtras().getSerializable(Constant.KEY_FRIENDS);
                    listFriends.addAll(friends);
                    ArrayList<String> agreeSourceId = new ArrayList<>();
                    for(Friends friend : friends){
                        agreeSourceId.add(friend.getFriendQQ());
                    }
                    listSourceId.removeAll(agreeSourceId);
                    if(contacts!=null){
                        resetConstacts();
                    }
                }else if(resultCode == RESULT_CODE_CONFIRM_FRIEND_REFUSE){
                    ArrayList<String> refuseSourceId = new ArrayList<>();
                    listSourceId.removeAll(refuseSourceId);
                }
        }
    }

    private void postImage() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, user.getQQnumber());
        params.add(Constant.KEY_IMAGE,imageBase64);
        client.post(Constant.HTTPURL_CHANGEMYDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    JSONObject object = new JSONObject(result);
                    if(object.getInt("success") == 0){
                        MyToast.showToast(MainUI.this, "修改成功",R.mipmap.right, Toast.LENGTH_SHORT);
                        String imgPath = object.getString(Constant.KEY_IMAGE);
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getQQnumber()
                        ,user.getNickname(),Uri.parse(imgPath)));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                MyToast.showToast(MainUI.this, "内网错误请稍后重试",R.mipmap.error, Toast.LENGTH_SHORT);
            }
        });
    }

    private void resetConstacts() {
        contacts.onDestroyView();
        contacts.onDestroy();
        contacts.onDetach();
        contactsState = 1;
        setTabSelection(1);
    }

    private int settingState;
    private int contactsState;

    private void resetSetting() {
        setting.onDestroyView();
        setting.onDestroy();
        setting.onDetach();
        //setting = null;
        settingState = 1;
        setTabSelection(2);
    }


    private String sourceId, applyMessage;

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();

        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
            if (contactContentMessage.getOperation().
                    equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)) {
                sourceId = contactContentMessage.getSourceUserId();
                applyMessage = contactContentMessage.getMessage();
                if (setSourceId == null) {
                    setSourceId = new HashSet<>();
                    listSourceId = new ArrayList<>();
                }
                if (setMessage == null) {
                    setMessage = new HashSet<>();
                    listMessage = new ArrayList<>();
                }
                setSourceId.add(sourceId);
                setMessage.add(applyMessage);
                listSourceId.addAll(setSourceId);
                listMessage.addAll(setMessage);
                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet(Constant.KEY_SET_SOURCEID, setSourceId);
                editor.putStringSet(Constant.KEY_SET_MESSAGE, setMessage);
                editor.apply();
            }else if(contactContentMessage.getOperation().
                    equals(ContactNotificationMessage.CONTACT_OPERATION_ACCEPT_RESPONSE)){
                String targetQQ = contactContentMessage.getSourceUserId();
                Log.d("messageContent",targetQQ);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add(Constant.KEY_QQNUMBER,targetQQ);
                Log.d("messageContent","run here");
                client.post(Constant.HTTPURL_GET_FRIEND_INFO, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        try {
                            Log.d("messageContent","run here");
                            JSONObject object = new JSONObject(new String(bytes));
                            Friends friends = new Friends(object.getString(Constant.KEY_QQNUMBER),
                                    object.getString(Constant.KEY_NICK),
                                    object.getString(Constant.KEY_SEX),
                                    object.getString(Constant.KEY_PHONE),
                                    object.getString(Constant.KEY_TOKEN),
                                    object.getString(Constant.KEY_IMAGE),
                                    object.getString(Constant.KEY_SIGNATURE));

                            listFriends.add(friends);
                            App.friendsList.add(friends);
                            resetConstacts();
                        } catch (JSONException e) {
                            Log.d("messageContent","run here1");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Log.d("messageContent","run here2");
                        Log.d("exception","内网错误");
                    }
                });
                return true;
            }

        }else if(messageContent instanceof DiscussionNotificationMessage){
            RongIM.getInstance().getDiscussion(message.getTargetId(), new RongIMClient.ResultCallback<Discussion>() {
                @Override
                public void onSuccess(Discussion discussion) {
                    Log.d("discussion","收到了讨论组的通知消息");
                    RongIM.getInstance().refreshDiscussionCache(discussion);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
        return false;
    }


    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        if (uiConversation.getMessageContent() instanceof ContactNotificationMessage) {
            ContactNotificationMessage message = (ContactNotificationMessage) uiConversation.getMessageContent();
            if (message.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)) {
                Intent intent = new Intent(this, ConfirmFriend.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_QQNUMBER, user.getQQnumber());
                bundle.putStringArrayList(Constant.KEY_SET_SOURCEID, listSourceId);
                bundle.putStringArrayList(Constant.KEY_SET_MESSAGE, listMessage);
                intent.putExtras(bundle);
                startActivityForResult(intent,Constant.REQUEST_CODE_CONFIRM_FRIEND);
            }
            return true;
        }
        return false;
    }


}
