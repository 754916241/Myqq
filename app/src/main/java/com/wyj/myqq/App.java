package com.example.wyj.myqq;

import android.app.Application;
import android.net.Uri;


import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.bean.User;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wyj on 2016/5/29.
 */
public class App extends Application {

    public static User user;
    public static ArrayList<Friends> friendsList;

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
