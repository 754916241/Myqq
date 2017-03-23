package com.wyj.myqq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.MyToast;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.wyj.myqq.utils.Constant.MOB_APP_KEY;
import static com.wyj.myqq.utils.Constant.MOB_APP_SECRETE;

public class ChangePwdOrPhone extends AppCompatActivity {

    private EditText edtPassword, repassword;
    private TextView tvGetCode,tvToast;
    private TextView tvSave, tvCancel;
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
                            // MyToast.showToast(Regist.this,"验证码输入正确",Toast.LENGTH_SHORT);
                            postInformation(phone);
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
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        if(bundle.getString(Constant.KEY_PHONE)!=null){
            phone = bundle.getString(Constant.KEY_PHONE);
        }
        if(bundle.getString(Constant.KEY_PASSWORD)!=null){
            password = bundle.getString(Constant.KEY_PASSWORD);
        }
        initView();
        SMSSDK.initSDK(this, MOB_APP_KEY, MOB_APP_SECRETE);
        //checkSdkVersion();
        SMSSDK.registerEventHandler(eh);
        initClick();
    }

    private void initClick() {
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SMSSDK.getVerificationCode(Constant.COUNTRY_ID_DEFAULT,phone);
                new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvGetCode.setClickable(false);
                        tvGetCode.setText(millisUntilFinished/1000+"s"+"后重新发送");
                    }

                    @Override
                    public void onFinish() {
                        tvGetCode.setClickable(true);
                        tvGetCode.setText("重新获取验证码");
                    }
                }.start();

            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone!=null){
                    SMSSDK.submitVerificationCode(Constant.COUNTRY_ID_DEFAULT,phone,repassword.getText().toString());
                }else if(password!=null){
                    if(edtPassword.getText().toString().equals(password)){
                        postInformation("");
                    }else{
                        MyToast.showToast(ChangePwdOrPhone.this, "密码输入错误,请核对后重试",R.mipmap.error ,Toast.LENGTH_SHORT);
                    }
                } else {
                    if (edtPassword.getText().toString().equals(repassword.getText().toString())) {
                        postInformation(null);
                    } else {
                        MyToast.showToast(ChangePwdOrPhone.this, "两次密码输入不一致，请核对后重试",R.mipmap.error ,Toast.LENGTH_SHORT);
                    }
                }

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(repassword.getWindowToken(), 0);
                onBackPressed();

            }
        });

    }

    private void initView() {
        edtPassword = (EditText) findViewById(R.id.edt_password);
        repassword = (EditText) findViewById(R.id.edt_repassword);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        tvToast = (TextView) findViewById(R.id.tv_toast);
        tvSave = (TextView) findViewById(R.id.tv_right);
        tvCancel = (TextView) findViewById(R.id.tv_left);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tvSave.setText("保存");
        dialog = new ProgressDialog(this);
        if(phone!=null){
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            repassword.setInputType(InputType.TYPE_CLASS_TEXT);
            edtPassword.setText(phone);
            edtPassword.setFocusable(false);
            edtPassword.setFocusableInTouchMode(false);
            tvToast.setText("如果您想更改绑定手机号请点击获取验证码，我们会发送验证码至您绑定的手机号以便于验证");
            repassword.setHint("请输入验证码");
            tvGetCode.setVisibility(View.VISIBLE);
        }
        if(password!=null){
            repassword.setInputType(InputType.TYPE_CLASS_TEXT);
            tvToast.setText("请输入密码进行验证，输入新手机号以完成修改");
            edtPassword.setHint("请输入密码");
            repassword.setHint("请输入新手机号");
        }

    }

    private void postInformation(String phone) {
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        if(phone!=null){
            params.add(Constant.KEY_PHONE, repassword.getText().toString());
        }else{
            params.add(Constant.KEY_PASSWORD, repassword.getText().toString());
        }

        client.post(Constant.HTTPURL_CHANGEMYDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                final JSONArray array;
                try {
                    array = new JSONArray(result);
                    JSONObject userObject = array.getJSONObject(0);
                    success = userObject.getInt("success");
                    if(success!=0){
                        Toast.makeText(ChangePwdOrPhone.this,"外部网络错误",Toast.LENGTH_LONG).show();
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 1000);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(ChangePwdOrPhone.this, "内部网络错误请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }
}
