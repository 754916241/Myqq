package com.example.festival_ui.biz;

import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import com.example.festival_ui.SendMsg;
import com.example.festival_ui.bean.SendedMsg;
import com.example.festival_ui.db.SmsProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by wyj on 2016/7/4.
 */
public class SmsBiz {

    private Context context;
    public SmsBiz(Context context){
        this.context = context;
    }

    public int sendMsg(String number, String msg, PendingIntent send,PendingIntent deliver){
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> contents = manager.divideMessage(msg);
        for (String content:contents){
            manager.sendTextMessage(number,null,content,send,deliver);
        }
        return contents.size();
    }

    public int sendMsg(Set<String> numbers, SendedMsg msg, PendingIntent send, PendingIntent deliver){
       save(msg);
        int result = 0;
        for (String number:numbers){
            int count = sendMsg(number,msg.getMsg(),send,deliver);
            result+=count;
        }
        return result;
    }

    private void save(SendedMsg msg){
        msg.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(SendedMsg.COLUMN_DATE,msg.getDate().getTime());
        values.put(SendedMsg.COLUMN_FES_NAME,msg.getFestivalName());
        values.put(SendedMsg.COLUMN_MSG,msg.getMsg());
        values.put(SendedMsg.COLUMN_NAMES,msg.getNames());
        values.put(SendedMsg.COLUMN_NUMBERS,msg.getNumbers());
        context.getContentResolver().insert(SmsProvider.URI_SMS_ALL,values);
    }

}
