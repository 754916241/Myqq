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
            activityStack=new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 退出除了cls以外的其他activity
     * @param cls
     */
    public void popAllActivityExceptOne(Class cls){
        while(true){
            if(activityStack.lastElement().getClass().equals(cls) ){
                break;
            }
            popActivity(activityStack.lastElement());
        }
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity(){

        //无法遍历栈结构，只会弹出最先进去的
        /*for (Activity activity: activityStack) {
            Log.d("ScreenManager",activityStack.size()+"");
            Log.d("ScreenManager",activity.getLocalClassName());

        }*/

        while (!activityStack.empty()){
            Log.d("ScreenManager",activityStack.lastElement().getLocalClassName());
            popActivity(activityStack.lastElement());
        }

        //这种方法会报异常，因为lastElement不存在
        /*while(true){
            if(activityStack.lastElement() == null){
                break;
            }
            popActivity(activityStack.lastElement());
        }*/
    }
}
