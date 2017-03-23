package com.example.wyj.myqq;

import android.app.Application;


import io.rong.imkit.RongIM;

/**
 * Created by wyj on 2016/5/29.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }
}
