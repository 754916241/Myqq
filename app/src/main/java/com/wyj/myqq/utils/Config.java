package com.wyj.myqq.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.example.wyj.myqq.R;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wyj on 2017/3/17.
 */

public class Config {

    public static int generateqqNumber() {
        String uuid = UUID.randomUUID().toString(); // xxxx-xxx-xxx-xxxx-xxx
        int hashCode = Math.abs(uuid.hashCode());
        return  hashCode;
    }

    /**
     * 判断电话号码是否符合格式.
     *
     * @param inputText the input text
     * @return true, if is phone
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    public static void setNotificationBar(Activity activity,int resColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Config.setTranslucentStatus(activity,true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(resColor);//通知栏所需颜色
    }

    /**
     * 设置导航栏颜色
     * @param activity
     * @param on
     */

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
