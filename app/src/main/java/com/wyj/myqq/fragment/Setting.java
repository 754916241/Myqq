package com.wyj.myqq.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ImageUtils;



public class Setting extends Fragment implements View.OnClickListener{


    private Button btnMore;
    private User user;
    private ImageView imgHead;

    private TextView tvQQnumber,tvNick,tvSex,tvSignature,tvAge;
    private RelativeLayout layoutHead,layoutNick,layoutSex,layoutSignature,layoutAge;
    private Bitmap bm;
    private String imgBase64,imgUriString;

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
            if(getArguments().getString(Constant.KEY_IMAGE_BASE64)!=null){
                imgBase64 = getArguments().getString(Constant.KEY_IMAGE_BASE64);
                imgHead.setImageBitmap(ImageUtils.stringToBitmap(imgBase64));
            }else if(getArguments().getString(Constant.KEY_IMAGE_URI)!=null){
                imgUriString = getArguments().getString(Constant.KEY_IMAGE_URI);
                imgHead.setImageBitmap(ImageUtils.compressBitmap(getActivity(),
                        Uri.parse(imgUriString),imgHead));
            }else{
                /*final Handler handler = new Handler(){
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
                }.start();*/
                Glide.with(this)
                        .load(user.getImage())
                        .placeholder(R.drawable.qq_icon)
                        .crossFade()
                        .into(imgHead);
            }

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
    public void onClick(View v) {
        if(onSettingListener!=null){
            onSettingListener.settingClick(v);
        }
    }
}
