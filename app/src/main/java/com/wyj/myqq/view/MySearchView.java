package com.wyj.myqq.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.wyj.myqq.R;

/**
 * Created by wyj on 2017/3/26.
 */

public class MySearchView extends SearchView {
    public MySearchView(Context context) {
        this(context,null);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        customizeSearchTextSize();
        customizeSearchButton();
    }

    private void customizeSearchTextSize(){
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) findViewById(getResources().getIdentifier("android:id/search_src_text",null,null));
        searchTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,searchTextView.getTextSize()*0.7f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMargins(10,0,0,0);
        searchTextView.setLayoutParams(lp);
    }


    private void customizeSearchButton(){
        View searchButton = findViewById(getResources().getIdentifier("android:id/search_mag_icon",null,null));
        LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        searchButton.setLayoutParams(lpImg);
        searchButton.setBackgroundResource(R.mipmap.icon_search);
       // View searchPlate = findViewById(getResources().getIdentifier("android:id/search_plate",null,null));
    }

}
