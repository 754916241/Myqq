package com.wyj.myqq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.wyj.myqq.utils.Constant.MOB_APP_KEY;
import static com.wyj.myqq.utils.Constant.MOB_APP_SECRETE;

public class ChangePwdOrPhone extends AppCompatActivity {

    private EditText edtPassword, edtRepassword;
    private TextView tvGetCode,tvToast;
    private TextView tvSave, tvCancel,tvTitle;
    private String qqnumber,phone,password;
    private Bundle bundle;
    private int success;
    private ProgressDialog dialog;
    private InputMethodManager imm;


    private EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            postInformation();
                        }
                    });
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(ChangePwdOrPhone.this,"获取验证码成功",Toast.LENGTH_SHORT);
                        }
                    });

                }
            }else{
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(ChangePwdOrPhone.this, "验证码输入错误", Toast.LENGTH_SHORT);

                        }
                    });
                }
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                ((Throwable)data).printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwdorphone);
        Config.setNotificationBar(this,R.color.colorApp);
        initData();
        initView();
        SMSSDK.initSDK(this, MOB_APP_KEY, MOB_APP_SECRETE);
        SMSSDK.registerEventHandler(eh);
        initClick();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        if(bundle.getString(Constant.KEY_PHONE)!=null){
            phone = bundle.getString(Constant.KEY_PHONE);
        }
        if(bundle.getString(Constant.KEY_PASSWORD)!=null){
            password = bundle.getString(Constant.KEY_PASSWORD);
        }
    }

    private void initClick() {
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
                if(Config.isPhone(edtPassword.getText().toString())) {
                    SMSSDK.getVerificationCode(Constant.COUNTRY_ID_DEFAULT, edtPassword.getText().toString());
                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvGetCode.setClickable(false);
                            tvGetCode.setText(millisUntilFinished / 1000 + "s" + "后重新发送");
                        }

                        @Override
                        public void onFinish() {
                            tvGetCode.setClickable(true);
                            tvGetCode.setText("重新获取验证码");
                        }
                    }.start();
                }else{
                    MyToast.showToast(ChangePwdOrPhone.this, "手机号码格式不正确",Toast.LENGTH_LONG);
                }
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone!=null){
                    SMSSDK.submitVerificationCode(Constant.COUNTRY_ID_DEFAULT,edtPassword.getText().toString(),edtRepassword.getText().toString());
                }else {
                    if (edtPassword.getText().toString().equals(edtRepassword.getText().toString())) {
                        //修改密码
                        postInformation();
                    } else {
                        MyToast.showToast(ChangePwdOrPhone.this, "两次密码输入不一致，请核对后重试",R.mipmap.error ,Toast.LENGTH_SHORT);
                    }
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(edtRepassword.getWindowToken(), 0);
                onBackPressed();

            }
        });

    }

    private void initView() {
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtRepassword = (EditText) findViewById(R.id.edt_repassword);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        tvToast = (TextView) findViewById(R.id.tv_toast);
        tvSave = (TextView) findViewById(R.id.tv_right);
        tvCancel = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvSave.setText("保存");

        dialog = new ProgressDialog(this);
        if(phone!=null){
            //修改绑定手机号
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            edtRepassword.setInputType(InputType.TYPE_CLASS_TEXT);
            tvTitle.setText("修改绑定手机号");
            tvToast.setText("在您输入新手机号后，我们会发送验证码进行验证");
            edtPassword.setHint("请输入新手机号");
            edtRepassword.setHint("请输入验证码");
            tvGetCode.setVisibility(View.VISIBLE);
        }else{
            tvTitle.setText("修改密码");
        }
    }

    private void postInformation() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        if(phone!=null){
            imm.hideSoftInputFromWindow(edtRepassword.getWindowToken(), 0);
            params.add(Constant.KEY_PHONE, edtRepassword.getText().toString());
        }else{
            params.add(Constant.KEY_PASSWORD, edtRepassword.getText().toString());
        }

        client.post(Constant.HTTPURL_CHANGEMYDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    JSONObject userObject = new JSONObject(result);
                    success = userObject.getInt("success");
                    if(success!=0){
                        MyToast.showToast(ChangePwdOrPhone.this,"传入信息有误请核对后重试",R.mipmap.error,Toast.LENGTH_SHORT);
                    }else{

                        if(userObject.getString("arg").equals(Constant.KEY_PASSWORD)){
                            Intent intent = new Intent();
                            setResult(Constant.RESULT_CODE_CHANGEPASSWORD, intent);
                            MyToast.showToast(ChangePwdOrPhone.this,"修改成功！请重新登陆",Toast.LENGTH_LONG);
                            finish();
                        }else{
                            Intent intent = new Intent();
                            intent.putExtra(Constant.KEY_PHONE, userObject.getString("arg"));
                            setResult(Constant.RESULT_CODE_CHANGEPHONE, intent);
                            finish();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                MyToast.showToast(ChangePwdOrPhone.this,"内网错误请稍后重试",R.mipmap.error,Toast.LENGTH_SHORT);
            }
        });



    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }
}
