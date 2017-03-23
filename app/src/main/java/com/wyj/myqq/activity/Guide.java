package com.wyj.myqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyj on 2016/7/2.
 */
public class Guide extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ViewPager vp;
    private GuideAdapter adapter;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.img_guide_point1,R.id.img_guide_point2};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.viewpager_guide);
        initView();
        initDots();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.viewpager_item_two,null));
        views.add(inflater.inflate(R.layout.viewpager_item_three,null));
        adapter = new GuideAdapter(views,this);
        vp = (ViewPager) findViewById(R.id.viewpager_guide);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(this);
    }
    public void startClick(View v){
        startActivity(new Intent(Guide.this,Login.class));
        this.finish();
    }

    private void initDots(){
        dots = new ImageView[views.size()];
        for (int i = 0;i<views.size();i++){
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0;i<views.size();i++){
           if(position == i){
               dots[i].setImageResource(R.mipmap.guide_point_selected);
           }else{
               dots[i].setImageResource(R.mipmap.guide_point);
           }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
