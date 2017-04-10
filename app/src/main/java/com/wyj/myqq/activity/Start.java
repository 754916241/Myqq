package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Constant;

public class Start extends AppCompatActivity {


    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.viewpager_item_one);
        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
        if(sp.getBoolean(Constant.KEY_IS_FIRST,true)){
            initData();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(Start.this,Guide.class));
                    Start.this.finish();
                }
            }, 1500);
        }else{
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(Start.this,Login.class));
                    Start.this.finish();
                }
            }, 1500);
        }


    }

    private void initData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.KEY_IS_FIRST,false);
        editor.apply();
    }
}
