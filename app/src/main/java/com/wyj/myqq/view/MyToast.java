package com.wyj.myqq.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;

/**
 * Created by wyj on 2017/3/17.
 */

public class MyToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MyToast(Context context) {
        super(context);
    }

    public static void showToast(Context context,String text,int iconId,int duration){
        MyToast toast = new MyToast(context);
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_item,null);
        TextView tv = (TextView) toastView.findViewById(R.id.tv);
        ImageView iv = (ImageView) toastView.findViewById(R.id.iv);
        tv.setText(text);
        iv.setBackgroundResource(iconId);
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER, 0, 350);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showToast(Context context,String text,int duration){
        MyToast toast = new MyToast(context);
        View toastView = LayoutInflater.from(context).inflate(R.layout.toast_item,null);
        TextView tv = (TextView) toastView.findViewById(R.id.tv);
        ImageView iv = (ImageView) toastView.findViewById(R.id.iv);
        tv.setText(text);
        iv.setVisibility(View.GONE);
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER, 0, 80);
        toast.setDuration(duration);
        toast.show();
    }

}
