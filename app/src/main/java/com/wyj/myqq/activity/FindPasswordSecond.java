package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyj.myqq.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.utils.ScreenManager;
import com.wyj.myqq.view.MyToast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.wyj.myqq.utils.Constant.MOB_APP_KEY;
import static com.wyj.myqq.utils.Constant.MOB_APP_SECRETE;

public class FindPasswordSecond extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title;

    private TextView tvDescription;
    /**
     * 请输入验证码
     */
    private EditText edtCode;
    /**
     * 获取验证码
     */
    private TextView tvGetCode;
    /**
     * 确定
     */
    private Button btnNext;

    private InputMethodManager imm;
    private Bundle bundle;
    private String qqnumber, phone;
    private boolean isPwdVisible = false;

    private EventHandler eh = new EventHandler() {

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
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(FindPasswordSecond.this, "获取验证码成功", Toast.LENGTH_SHORT);
                        }
                    });

                }
            } else {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(FindPasswordSecond.this, "验证码输入错误", Toast.LENGTH_SHORT);

                        }
                    });
                }
                ((Throwable) data).printStackTrace();
            }
        }
    };

    /**
     * 重置密码
     */
    private EditText edtPwd;
    private ImageView imgPwdVis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_second);
        Config.setNotificationBar(this, R.color.colorApp);
        initData();
        initView();
    }

    private void initData() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScreenManager.getScreenManager().pushActivity(this);
        bundle = getIntent().getExtras();
        phone = bundle.getString(Constant.KEY_PHONE);
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        SMSSDK.initSDK(this, MOB_APP_KEY, MOB_APP_SECRETE);
        SMSSDK.registerEventHandler(eh);
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        edtCode = (EditText) findViewById(R.id.edt_code);
        tvGetCode = (TextView) findViewById(R.id.tv_getCode);
        tvGetCode.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        title.setText("获取验证码");
        imgLeft.setVisibility(View.VISIBLE);
        tvDescription.setText("您的手机号是"+phone.substring(0,7)+"****"+",请获取验证码");
        edtPwd = (EditText) findViewById(R.id.edt_pwd);
        imgPwdVis = (ImageView) findViewById(R.id.img_pwdVis);
        imgPwdVis.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                imm.hideSoftInputFromWindow(edtCode.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.tv_getCode:
                imm.hideSoftInputFromWindow(edtCode.getWindowToken(), 0);

                SMSSDK.getVerificationCode(Constant.COUNTRY_ID_DEFAULT, phone);
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

                break;
            case R.id.btn_next:
                if (edtPwd.getText().toString().length() < 6) {
                    MyToast.showToast(FindPasswordSecond.this,"密码不能低于六位",R.mipmap.error,Toast.LENGTH_SHORT);
                }else{
                    SMSSDK.submitVerificationCode(Constant.COUNTRY_ID_DEFAULT, phone, edtCode.getText().toString());
                }

                break;
            case R.id.img_pwdVis:
             if(isPwdVisible){
                    imgPwdVis.setImageResource(R.mipmap.eye_invisible);
                    edtPwd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPwd.setSelection(edtPwd.getText().toString().length());
                    isPwdVisible = false;
                }else{
                    imgPwdVis.setImageResource(R.mipmap.eye_visible);
                    edtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    edtPwd.setSelection(edtPwd.getText().toString().length());
                    isPwdVisible = true;
                }
                break;
        }
    }

    private void postInformation() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        params.add(Constant.KEY_PASSWORD,edtPwd.getText().toString());
        client.post(Constant.HTTPURL_CHANGEMYDATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    JSONObject userObject = new JSONObject(result);
                    int success = userObject.getInt("success");
                    if (success != 0) {
                        MyToast.showToast(FindPasswordSecond.this, "传入信息有误请核对后重试", R.mipmap.error, Toast.LENGTH_SHORT);
                    } else {
                        ScreenManager.getScreenManager().popAllActivityExceptOne(FindPasswordFirst.class);
                        MyToast.showToast(FindPasswordSecond.this, "密码重置成功！", R.mipmap.right, Toast.LENGTH_SHORT);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                MyToast.showToast(FindPasswordSecond.this, "内网错误请稍后重试", R.mipmap.error, Toast.LENGTH_SHORT);
            }
        });
    }


    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
