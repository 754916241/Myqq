package com.wyj.myqq.dblocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wyj on 2017/3/27.
 */

public class DBLocal extends SQLiteOpenHelper {

    private static final String CREATE_RECORD_TABLE = "create table record("
            + "qqnumber varchar(20) default none,"
            + "password varchar(20) default none,"
            + "image varchar(10000) default none)";

    public DBLocal(Context context) {
        super(context, "myqq", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertToRecord(String qqnumber,String password,String image) {
        SQLiteDatabase write = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("qqnumber", qqnumber);
        cv.put("password", password);
        cv.put("image", image);
        write.insert("record", null, cv);
        cv.clear();
        write.close();
    }

    public void updateInRecord(String qqnumber,String password,String image) {
        SQLiteDatabase write = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", password);
        cv.put("image", image);
        write.update("record",cv,"qqnumber=?",new String[]{qqnumber});
        cv.clear();
        write.close();
    }

    public void deleteFromRecord(String qqnumber){
        SQLiteDatabase write = getWritableDatabase();
        write.delete("record", "qqnumber=?", new String[] {qqnumber});
        write.close();
    }


    public boolean isExistInRecord(String qqnumber){
        SQLiteDatabase read = getReadableDatabase();
        Cursor c = read.query("record", null, null, null, null, null, null);
        while (c.moveToNext()) {
            String oldQQ = c.getString(c.getColumnIndex("qqnumber"));
            if (qqnumber.equals(oldQQ)) {
                c.close();
                read.close();
                return true;
            }
        }
        c.close();
        read.close();
        return false;
    }

}
