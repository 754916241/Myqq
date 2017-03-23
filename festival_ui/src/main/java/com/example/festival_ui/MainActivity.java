package com.example.festival_ui;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.festival_ui.fragment.FestivalCategoryFragment;
import com.example.festival_ui.fragment.SmsHistoryFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout tableLayout;
    private ViewPager viewPager;
    private String[] titles = new String[]{"节日短信","发送记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        tableLayout = (TabLayout) findViewById(R.id.tab_main);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position==1)
                    return new SmsHistoryFragment();
                return new FestivalCategoryFragment();
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tableLayout.setupWithViewPager(viewPager);
    }
}
