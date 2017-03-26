package com.example.wyj.myqq;

import android.app.Application;
import android.net.Uri;


import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wyj on 2016/5/29.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
//        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String s) {
//                UserInfo userInfo = new UserInfo(s,"系统管理员", Uri.parse("http://192.168.0.108:82/htdocs/qq/0system.png"));
//                return userInfo;
//            }
//        },true);

    }
}
