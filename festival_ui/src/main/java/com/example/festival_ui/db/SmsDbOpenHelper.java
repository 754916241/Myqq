package com.example.festival_ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.festival_ui.bean.SendedMsg;

/**
 * Created by wyj on 2016/7/4.
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sms.db";
    private static final int DB_VERSION = 1;
    private SmsDbOpenHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    private static SmsDbOpenHelper helper;
    public static SmsDbOpenHelper getInstance(Context context){
        if(helper == null){
            synchronized (SmsDbOpenHelper.class){
                if(helper==null){
                    helper = new SmsDbOpenHelper(context);
                }
            }
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ SendedMsg.TABLE_NAME + " ( "+
                SendedMsg.COLUMN_DATE + "INTEGER , "+
                SendedMsg.COLUMN_FES_NAME + "text , "+
                SendedMsg.COLUMN_MSG + "text , "+
                SendedMsg.COLUMN_NAMES + "text , "+
                SendedMsg.COLUMN_NUMBERS + "text  "+
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
