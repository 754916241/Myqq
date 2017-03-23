package com.wyj.myqq.utils;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Created by wyj on 2017/3/21.
 */
public class ScreenManager {
    private static Stack<Activity> activityStack;
    private static ScreenManager instance;
    private  ScreenManager(){
    }
    public static ScreenManager getScreenManager(){
        if(instance==null){
            instance=new ScreenManager();
        }
        return instance;
    }
    //退出栈顶Activity
    public void popActivity(Activity activity){
        if(activity!=null){
            activity.finish();
            activityStack.remove(activity);
            activity=null;
        }
    }

    //获得当前栈顶Activity
    public Activity currentActivity(){
        Activity activity=activityStack.lastElement();
        return activity;
    }

    //将当前Activity推入栈中
    public void pushActivity(Activity activity){
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    //退出栈中所有Activity
    public void popAllActivityExceptOne(Class cls){
        while(true){
            Log.d("getLocalClassName",activityStack.lastElement().getLocalClassName());

            if(activityStack.lastElement()==null){
                break;
            }
            if(activityStack.lastElement().getClass().equals(cls) ){
                popActivity(activityStack.lastElement());
                break;
            }
            popActivity(activityStack.lastElement());
        }
    }
}
