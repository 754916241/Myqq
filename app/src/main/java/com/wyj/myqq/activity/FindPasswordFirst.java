package com.wyj.myqq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class FindPasswordFirst extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgLeft;
    /**
     * 联系人
     */
    private TextView title;
    /**
     * 账号
     */
    private EditText edtQqnumber;
    /**
     * 绑定手机号
     */
    private EditText edtPhone;

    private Button btnNext;
    private InputMethodManager imm;
    private String qqnumber,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_first);
        Config.setNotificationBar(this, R.color.colorApp);
        initView();

    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        imgLeft.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);
        edtQqnumber = (EditText) findViewById(R.id.edt_qqnumber);
        edtQqnumber.setOnClickListener(this);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtPhone.setOnClickListener(this);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        title.setText("验证手机号");
        imgLeft.setVisibility(View.VISIBLE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ScreenManager.getScreenManager().pushActivity(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left:
                imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_next:
                qqnumber = edtQqnumber.getText().toString();
                phone = edtPhone.getText().toString();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.add(Constant.KEY_QQNUMBER,qqnumber);
                client.post(Constant.HTTPURL_GET_FRIEND_INFO, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        try {
                            JSONObject object = new JSONObject(new String(bytes));
                            int status = object.getInt("status");
                            if(status == 0){
                                //qq号存在
                                String phone = object.getString(Constant.KEY_PHONE);
                                if(phone.equals(FindPasswordFirst.this.phone)){
                                    //手机号匹配
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.KEY_QQNUMBER,qqnumber);
                                    bundle.putString(Constant.KEY_PHONE,phone);
                                    Intent intent = new Intent(FindPasswordFirst.this,FindPasswordSecond.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    MyToast.showToast(FindPasswordFirst.this,"手机号不匹配，请核对后重试",R.mipmap.error,Toast.LENGTH_SHORT);
                                }
                            }else{
                                //qq号不存在
                                MyToast.showToast(FindPasswordFirst.this,"qq号不存在，请核对后重试",R.mipmap.error,Toast.LENGTH_SHORT);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        MyToast.showToast(FindPasswordFirst.this,"内部网络错误请稍后重试",R.mipmap.error,Toast.LENGTH_SHORT);
                    }
                });
                break;
        }
    }
}
