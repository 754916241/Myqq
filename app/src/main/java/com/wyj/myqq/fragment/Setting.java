package com.wyj.myqq.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyj.myqq.R;

import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;



/**
 * A simple {@link Fragment} subclass.
 */
public class Setting extends Fragment implements View.OnClickListener{


    private Button btnMore;
    private User user;
    private ImageView imgHead;

    private TextView tvQQnumber,tvNick,tvSex,tvSignature,tvAge;
    private RelativeLayout layoutHead,layoutNick,layoutSex,layoutSignature,layoutAge;
    private Bitmap bm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View settingLayout =inflater .inflate(R.layout.fragment_setting, container, false);

        btnMore = (Button) settingLayout.findViewById(R.id.btn_more);
        imgHead = (ImageView) settingLayout.findViewById(R.id.img_setting_head);
        tvQQnumber = (TextView) settingLayout.findViewById(R.id.tv_setting_qqnumber);
        tvNick = (TextView) settingLayout.findViewById(R.id.tv_setting_nick);
        tvSex = (TextView) settingLayout.findViewById(R.id.tv_setting_sex);
        tvAge = (TextView) settingLayout.findViewById(R.id.tv_setting_age);
        tvSignature = (TextView) settingLayout.findViewById(R.id.tv_setting_signature);
        layoutHead = (RelativeLayout) settingLayout.findViewById(R.id.layout_image);
        layoutAge = (RelativeLayout) settingLayout.findViewById(R.id.layout_age);
        layoutSex = (RelativeLayout) settingLayout.findViewById(R.id.layout_sex);
        layoutNick = (RelativeLayout) settingLayout.findViewById(R.id.layout_nick);
        layoutSignature = (RelativeLayout) settingLayout.findViewById(R.id.layout_signature);

        layoutHead.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        layoutNick.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
        layoutSignature.setOnClickListener(this);
        layoutAge.setOnClickListener(this);

        if(getArguments()!=null){
            user = (User) getArguments().getSerializable(Constant.KEY_USER);
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0){
                        imgHead.setImageBitmap(bm);
                    }
                }
            };

            new Thread(){
                @Override
                public void run() {
                    bm = ImageUtils.receiveImage(user.getImage());
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }.start();

            tvAge.setText(user.getAge());
            tvQQnumber.setText(user.getQQnumber());
            tvNick.setText(user.getNickname());
            tvSex.setText(user.getSex());
            tvSignature.setText(user.getSignature());

        }


        return settingLayout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onSettingListener = (OnSettingListener) context;
    }

    public interface OnSettingListener {
        void settingClick(View view);
    }

    public void setOnSettingListener(OnSettingListener onSettingListener) {
        this.onSettingListener = onSettingListener;
    }

    private OnSettingListener onSettingListener;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("您希望：");
        menu.add(1,1,1,"修改我的密码");
        menu.add(1,2,1,"修改我的昵称");
        menu.add(1,3,1,"修改我的真实姓名");
        menu.add(1,4,1,"修改我的年龄");
        menu.add(1,5,1,"修改我的性别");
        menu.add(1,6,1,"修改我的个性签名");
    }


    @Override
    public void onClick(View v) {
        if(onSettingListener!=null){
            onSettingListener.settingClick(v);
        }
    }
}

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constant.REQUEST_CODE_CHANGENICK){
            if(resultCode==Constant.RESULT_CODE_CHANGENICK){
                //adapter.getItem(0).getNickname()
            }
        }
    }*/
