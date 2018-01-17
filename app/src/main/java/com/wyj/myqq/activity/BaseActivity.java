package com.wyj.myqq.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wyj.myqq.R;

public abstract class BaseActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
    private TextView title;
    private ImageView imgLeft,imgRight;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(R.id.activity_base);
    }

    private  void initContentView(int layoutId){
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        v = LayoutInflater.from(this).inflate(layoutId, parentLinearLayout, true);
    }

    @Override
    public void setContentView(int layoutResID) {

        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);
        findViewToControl();
    }

    @Override
    public void setContentView(View view) {
        parentLinearLayout.addView(view);
        findViewToControl();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        parentLinearLayout.addView(view, params);
        findViewToControl();
    }

    private void findViewToControl(){
        title = (TextView) v.findViewById(R.id.tv_title);
        imgLeft = (ImageView) v.findViewById(R.id.img_left);
        imgRight = (ImageView) v.findViewById(R.id.img_right);
    }

    protected void setTitle(String str){
        title.setText(str);
    }


    protected void setLeftBtnVisibility(boolean isVisibility){
        int status = isVisibility ? View.VISIBLE : View.INVISIBLE;
        imgLeft.setVisibility(status);
    }

    protected void setRightBtnVisibility(boolean isVisibility){
        int status = isVisibility ? View.VISIBLE : View.INVISIBLE;
        imgRight.setVisibility(status);
    }

    protected void setLeftBtnOnClickListener(View.OnClickListener listener){
        imgLeft.setOnClickListener(listener);
    }

    protected void setRightOnClickListener(View.OnClickListener listener){
        imgRight.setOnClickListener(listener);
    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
