package com.wyj.myqq.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.wyj.myqq.bean.User;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.DataCleanManager;
import com.wyj.myqq.utils.MyToast;
import com.wyj.myqq.utils.ScreenManager;

import static org.xmlpull.v1.XmlPullParser.TEXT;

public class MoreSetting extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;
    private TextView tvPassword,tvAbout,tvSuggest;
    private RelativeLayout rlPhone,rlCache;
    private TextView tvPhone,tvCache;
    private EditText edtDialog;
    private Bundle bundle;
    private User user;
    private String qqnumber,password,phone;
    private Button btnExit;
    private ProgressDialog dialog;
    private ScreenManager screenManager;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        initData();
        initView();
        initClick();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(Constant.KEY_USER);
        qqnumber = user.getQQnumber();
        password = bundle.getString(Constant.KEY_PASSWORD);
        phone = user.getPhone();
        screenManager = ScreenManager.getScreenManager();
        screenManager.pushActivity(this);
    }

    private void initClick() {
        tvPassword.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvSuggest.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlCache.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvPassword = (TextView) findViewById(R.id.tv_password);
        tvAbout = (TextView) findViewById(R.id.tv_about);
        tvSuggest = (TextView) findViewById(R.id.tv_suggest);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvCache = (TextView) findViewById(R.id.tv_cache);
        rlPhone = (RelativeLayout) findViewById(R.id.layout_phone);
        rlCache = (RelativeLayout) findViewById(R.id.layout_cache);
        btnExit = (Button) findViewById(R.id.btn_exit);
        imgBack = (ImageView) findViewById(R.id.img_left);
        imgBack.setVisibility(View.VISIBLE);
        dialog = new ProgressDialog(this);

        tvTitle.setText("更多设置");
        tvPhone.setText(phone);
        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePwdOrPhone(String key,String value){
        Bundle bundle = new Bundle();
        if(key!=null){
            bundle.putString(key,value);
        }
        bundle.putString(Constant.KEY_QQNUMBER,qqnumber);
        Intent intent = new Intent(this,ChangePwdOrPhone.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder;

        switch (view.getId()){
            case R.id.tv_password:
                builder = new AlertDialog.Builder(this);
                edtDialog = new EditText(this);
                edtDialog.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setMessage("请输入您的密码以确认");
                builder.setView(edtDialog);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if(edtDialog.getText().toString().equals(password)){
                            changePwdOrPhone(null,null);

                        }else{
                            MyToast.showToast(MoreSetting.this, "密码错误，请验证后重试",R.mipmap.error, Toast.LENGTH_SHORT);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.tv_about:
                startActivity(new Intent(this,About.class));
                break;
            case R.id.tv_suggest:
                startActivityForResult(new Intent(this,Suggest.class),Constant.REQUEST_CODE_SUGGEST);
                break;
            case R.id.layout_phone:
                String[] items = new String[]{"通过密码修改", "通过绑定手机号修改"};
                new AlertDialog.Builder(this)
                        .setTitle("选择修改方式")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                switch (arg1) {
                                    case 0:
                                        changePwdOrPhone(Constant.KEY_PASSWORD,password);
                                        break;
                                    case 1:
                                        changePwdOrPhone(Constant.KEY_PHONE,phone);
                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.layout_cache:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("系统提示");
                builder.setMessage("确认清除所有缓存?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DataCleanManager.cleanApplicationData(getApplicationContext(),"");
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.btn_exit:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("系统提示");
                builder.setMessage("确认退出当前账号?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialog.setMessage("正在退出……");
                        dialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ScreenManager.getScreenManager().popAllActivityExceptOne(MainUI.class);
                                dialog.dismiss();
                            }
                        }, 1500);


                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.img_left:
                onBackPressed();
                break;
        }
    }
}
