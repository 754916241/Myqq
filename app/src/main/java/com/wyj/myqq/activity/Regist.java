package com.wyj.myqq.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.wyj.myqq.utils.ImageUtils;
import com.wyj.myqq.utils.MyToast;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.wyj.myqq.utils.Constant.MOB_APP_KEY;
import static com.wyj.myqq.utils.Constant.MOB_APP_SECRETE;

public class Regist extends AppCompatActivity implements TextWatcher{

    private EditText edtPhone, edtCode,edtPwd, edtRepwd;
    private TextView getCode;
    private ImageView imgHead;
    private Button regist,test;
    private String qqnumber, image, token;
    private int success;
    private Uri imageUri;
    private Bitmap photo = null;
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
                            postInformation();
                        }
                    });
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(Regist.this,"获取验证码成功",Toast.LENGTH_SHORT);
                        }
                    });

                }
            }else{
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.showToast(Regist.this, "验证码输入错误", Toast.LENGTH_SHORT);

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        setContentView(R.layout.activity_regist);
        SMSSDK.initSDK(this, MOB_APP_KEY, MOB_APP_SECRETE);
        checkSdkVersion();
        SMSSDK.registerEventHandler(eh);
        initView();
        initClick();
    }

    private void checkInformation() {
        if (edtPwd.getText().toString().equals("")||edtCode.getText().toString().equals("")
                ||edtPwd.getText().toString().equals("")) {
            regist.setBackgroundResource(R.drawable.button_unable_click);
            regist.setEnabled(false);
            regist.setSoundEffectsEnabled(false);
        }else{
            if(edtPwd.getText().toString().length() == edtRepwd.getText().toString().length()){
                regist.setBackgroundResource(R.drawable.button_select);
                regist.setEnabled(true);
                regist.setSoundEffectsEnabled(true);
            }else{
                regist.setBackgroundResource(R.drawable.button_unable_click);
                regist.setEnabled(false);
                regist.setSoundEffectsEnabled(false);
            }
        }
    }

    private void initView() {
        edtPhone = (EditText) findViewById(R.id.edt_regist_phone);
        edtCode = (EditText) findViewById(R.id.edt_regist_code);
        edtPwd = (EditText) findViewById(R.id.edt_regist_pwd);
        edtRepwd = (EditText) findViewById(R.id.edt_regist_repwd);
        getCode = (TextView) findViewById(R.id.tv_regist_getCode);
        imgHead = (ImageView) findViewById(R.id.img_regist_addhead);
        regist = (Button) findViewById(R.id.btn_regist_regist);
        test = (Button) findViewById(R.id.button);
        imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        edtPwd.addTextChangedListener(this);
        edtRepwd.addTextChangedListener(this);
        edtCode.addTextChangedListener(this);
        dialog = new ProgressDialog(this);
    }

    private void initClick() {

        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.wyj.myqq.utils.ImageUtils.showImagePickDialog(Regist.this);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.submitVerificationCode(Constant.COUNTRY_ID_DEFAULT,edtPhone.getText()
                        .toString(),edtCode.getText().toString());

            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);

                    if(Config.isPhone(edtPhone.getText().toString())){

                       SMSSDK.getVerificationCode(Constant.COUNTRY_ID_DEFAULT,edtPhone.getText().toString());
                        CountDownTimer countDownTimer = new CountDownTimer(60000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                getCode.setClickable(false);
                                getCode.setText(millisUntilFinished/1000+"s"+"后重新发送");
                            }

                            @Override
                            public void onFinish() {
                                getCode.setClickable(true);
                                getCode.setText("重新获取验证码");
                            }
                        }.start();

                    }else{
                        MyToast.showToast(Regist.this, "手机号码格式不正确",Toast.LENGTH_LONG);
                    }

            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(edtPwd.getText().toString().equals(edtRepwd.getText().toString()))) {
                    MyToast.showToast(Regist.this, "两次密码输入不一致，请检查后重新输入",R.mipmap.error, Toast.LENGTH_SHORT);
                }
                else {
                    dialog.setMessage("正在从网络中获取数据，请稍后");
                    dialog.show();
                    SMSSDK.submitVerificationCode(Constant.COUNTRY_ID_DEFAULT,edtPhone.getText()
                            .toString(),edtCode.getText().toString());

                }
            }
        });
    }

    private void postInformation() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, "" + Config.generateqqNumber());
        params.add(Constant.KEY_PASSWORD, edtPwd.getText().toString());
        params.add(Constant.KEY_PHONE, edtPhone.getText().toString());

        if (photo == null) {
            photo = BitmapFactory.decodeResource(getResources(), R.drawable.qq_icon);
        }
        params.add(Constant.KEY_IMAGE, ImageUtils.sendImage(photo));

        client.post(Constant.HTTPURL_REGIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String response = new String(bytes);
                Log.d("response", response);

                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object = array.getJSONObject(0);
                    success = object.getInt("success");
                    qqnumber = object.getString(Constant.KEY_QQNUMBER);
                    image = object.getString(Constant.KEY_IMAGE);
                    token = object.getString(Constant.KEY_TOKEN);
                    if (success == 1) {
                        dialog.dismiss();
                        MyToast.showToast(Regist.this, "外部网络错误请稍后重试",R.mipmap.error, Toast.LENGTH_SHORT);
                    } else {
                        final Intent intent = new Intent(Regist.this, RegistSuccess.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_QQNUMBER, qqnumber);
                        intent.putExtras(bundle);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                dialog.dismiss();
                                startActivity(intent);
                                Regist.this.finish();
                            }
                        }, 2000);

                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                dialog.dismiss();
                MyToast.showToast(Regist.this, "内部网络错误请稍后重试",R.mipmap.error,Toast.LENGTH_SHORT);
            }
        });
    }

    private void checkSdkVersion(){
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUEST_CODE_FROM_ALBUM:
                imageUri = data.getData();
                ContentResolver resolver = getContentResolver();
                try {
                    photo = MediaStore.Images.Media.getBitmap(resolver, ImageUtils.convertUri(imageUri, this));
                    imgHead.setImageBitmap(photo);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case Constant.REQUEST_CODE_FROM_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromcamera);
                } else {
                    Bundle bundle = data.getExtras();
                    photo = bundle.getParcelable("data");
                    imgHead.setImageBitmap(photo);
                }
                break;

        }
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkInformation();
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}

/**
 * rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
 *
 * @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
 * if(checkedId == R.id.rb_regist_man){
 * sex = rbMan.getText().toString();
 * }else if(checkedId == R.id.rb_regist_woman){
 * sex = rbWoman.getText().toString();
 * }
 * }
 * });
 */

