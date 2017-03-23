package com.example.festival_ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.festival_ui.bean.FesitivalLab;
import com.example.festival_ui.bean.Festival;
import com.example.festival_ui.bean.Msg;
import com.example.festival_ui.bean.SendedMsg;
import com.example.festival_ui.biz.SmsBiz;
import com.example.festival_ui.view.FlowLayout;

import java.util.HashSet;

public class SendMsg extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private int festivalId,msgId;
    public static final String KEY_FESTIVAL_ID = "FESTIVALiD";
    public static final String KEY_MSG_ID = "msgId";
    private Festival festival;
    private Msg msg;
    private int msgSendCount,totalCount;
    private EditText edMsg;
    private Button btnAdd;
    private com.example.festival_ui.view.FlowLayout flcontacts;
    private FloatingActionButton send;
    private View loading;
    private LayoutInflater inflater;
    private static final String ACTION_SNED_MSG = "ACTION_SNED_MSG";
    private static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";
    private PendingIntent sendPi,deliverPi;
    private BroadcastReceiver sendBroadcastReceiver,deliverBroadcastReceiver;
    private SmsBiz biz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        inflater = LayoutInflater.from(this);
        biz = new SmsBiz(this);
        initDatas();
        initViews();
        initEvents();
        initReciver();
    }

    private void initReciver() {
        Intent sendIntent = new Intent(ACTION_SNED_MSG);
        sendPi = PendingIntent.getBroadcast(this,0,sendIntent,0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        deliverPi = PendingIntent.getBroadcast(this,0,deliverIntent,0);
        registerReceiver(sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(getResultCode() == RESULT_OK){

                }else{

                }
                Toast.makeText(SendMsg.this,"短信发送成功",Toast.LENGTH_SHORT).show();
                msgSendCount++;
                if(msgSendCount == totalCount){
                    finish();
                }
            }
        },new IntentFilter(ACTION_SNED_MSG));

        registerReceiver(deliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(getResultCode() == RESULT_OK){

                }else{

                }
            }
        },new IntentFilter(ACTION_DELIVER_MSG));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendBroadcastReceiver);
        unregisterReceiver(deliverBroadcastReceiver);
    }

    private void initEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactNums.size()==0){
                    Toast.makeText(SendMsg.this,"请先选择联系人",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(edMsg.getText().toString())){
                    Toast.makeText(SendMsg.this,"短信内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                loading.setVisibility(View.VISIBLE);
                totalCount = biz.sendMsg(contactNums,buildSendMsg(edMsg.getText().toString()),sendPi,deliverPi);
                msgSendCount = 0;
            }
        });
    }

    private SendedMsg buildSendMsg(String msg) {
        SendedMsg sendedMsg = new SendedMsg();
        sendedMsg.setMsg(msg);
        sendedMsg.setFestivalName(festival.getName());
        String names = "";
        for(String name:contactNames){
            names+=name+":";
        }
        sendedMsg.setNames(names.substring(0,names.length()-1));
        String numbers = "";
        for(String number:contactNums){
            numbers+=number+":";
        }
        sendedMsg.setNumbers(numbers.substring(0,names.length()-1));
        return sendedMsg;
    }

    private void initViews() {
        edMsg = (EditText) findViewById(R.id.edt_content);
        btnAdd = (Button) findViewById(R.id.btn_add);
        flcontacts = (FlowLayout) findViewById(R.id.fl_contacts);
        send = (FloatingActionButton) findViewById(R.id.float_toSend);
        loading = findViewById(R.id.layout_loading);
        loading.setVisibility(View.GONE);
        if(msgId!=-1){
            FesitivalLab.getInstance().getMsgById(msgId);
            edMsg.setText(msg.getContent());
        }
    }

    private void initDatas() {
        festivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID,-1);
        msgId = getIntent().getIntExtra(KEY_MSG_ID,-1);
       festival =  FesitivalLab.getInstance().getFestivalById(festivalId);
        setTitle(festival.getName());
    }

    public static void toActivity(Context context,int festivalId,int msgId){
        Intent intent = new Intent(context,SendMsg.class);
        intent.putExtra(KEY_FESTIVAL_ID,festivalId);
        intent.putExtra(KEY_MSG_ID,msgId);
        context.startActivity(intent);
    }


    private HashSet<String> contactNames = new HashSet<>();
    private HashSet<String> contactNums = new HashSet<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Uri contactUri = data.getData();
                Cursor c = getContentResolver().query(contactUri,null,null,null,null);
                c.moveToFirst();
                String contactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactNames.add(contactName);
                String number = getContactNumber(c);
                if(!TextUtils.isEmpty(number)){
                    contactNums.add(number);
                    contactNames.add(contactName);
                    addTag(contactName);
                }
            }
        }
    }

    private void addTag(String contactName) {
       TextView view = (TextView) inflater.inflate(R.layout.tag,flcontacts,false);
        view.setText(contactName);
        flcontacts.addView(view);
    }

    private String getContactNumber(Cursor c) {
        int numCount = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String number = null;
        if (numCount > 0) {
            int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor photoCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            photoCursor.moveToFirst();
            number = photoCursor.getString(photoCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            photoCursor.close();

        }
        c.close();
        return number;
    }
}

