package com.wyj.myqq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

public class ChangeMyData extends AppCompatActivity {

    private Bundle bundle;
    private String qqnumber, nickname, truename, sex, age, signature;
    private EditText editText;
    private ImageButton selectMan, selectWoman;
    private TextView tvSave, tvCancel, tvMan, tvWoman,tvTitle;
    private RelativeLayout layoutSex;
    private ProgressDialog dialog;
    private int success;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_data);
        initView();
        initData();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edt_change);
        selectMan = (ImageButton) findViewById(R.id.img_select_man);
        selectWoman = (ImageButton) findViewById(R.id.img_select_woman);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvCancel = (TextView) findViewById(R.id.tv_left);
        tvMan = (TextView) findViewById(R.id.tv_man);
        tvWoman = (TextView) findViewById(R.id.tv_woman);
        layoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dialog = new ProgressDialog(this);
    }

    private void initData() {
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);

        if (bundle.getString(Constant.KEY_NICK) != null) {
            nickname = bundle.getString(Constant.KEY_NICK);
            editText.setText(nickname);
            editText.setVisibility(View.VISIBLE);
            layoutSex.setVisibility(View.GONE);
            tvTitle.setText("昵称");
        }

        if (bundle.getString(Constant.KEY_AGE) != null) {
            age = bundle.getString(Constant.KEY_AGE);
            editText.setText(age);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setVisibility(View.VISIBLE);
            layoutSex.setVisibility(View.GONE);
            tvTitle.setText("年龄");
        }
        if (bundle.getString(Constant.KEY_SEX) != null) {
            sex = bundle.getString(Constant.KEY_SEX);
            editText.setVisibility(View.GONE);
            layoutSex.setVisibility(View.VISIBLE);
            tvTitle.setText("性别");
            if (sex.equals("男")) {
                selectMan.setVisibility(View.VISIBLE);
                selectWoman.setVisibility(View.GONE);
            } else {
                selectMan.setVisibility(View.GONE);
                selectWoman.setVisibility(View.VISIBLE);
            }
        }
        if (bundle.getString(Constant.KEY_SIGNATURE) != null) {
            signature = bundle.getString(Constant.KEY_SIGNATURE);
            editText.setVisibility(View.VISIBLE);
            layoutSex.setVisibility(View.GONE);
            editText.setText(signature);
            tvTitle.setText("个性签名");
        }

        tvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan.setVisibility(View.VISIBLE);
                selectWoman.setVisibility(View.GONE);
                sex = "男";
            }
        });

        tvWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMan.setVisibility(View.GONE);
                selectWoman.setVisibility(View.VISIBLE);
                sex = "女";
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickname != null) {
                    httpPost(Constant.HTTPURL_CHANGEMYDATA, Constant.KEY_NICK, editText.getText().toString(), Constant.RESULT_CODE_CHANGENICK);
                }
                if (sex != null) {
                    httpPost(Constant.HTTPURL_CHANGEMYDATA, Constant.KEY_SEX, sex, Constant.RESULT_CODE_CHANGESEX);
                }
                if (age != null) {
                    httpPost(Constant.HTTPURL_CHANGEMYDATA, Constant.KEY_AGE, editText.getText().toString(), Constant.RESULT_CODE_CHANGEAGE);
                }
                if (signature != null) {
                    httpPost(Constant.HTTPURL_CHANGEMYDATA, Constant.KEY_SIGNATURE, editText.getText().toString(), Constant.RESULT_CODE_CHANGESIGNATURE);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                onBackPressed();


            }
        });

    }

    private void httpPost(String url, final String key, final String values, final int resultCode) {
        dialog.setMessage("正在提交，请稍后……");
        dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add(Constant.KEY_QQNUMBER, qqnumber);
        params.add(key, values);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String result = new String(bytes);
                try {
                    Log.e("result", result);

                    JSONObject userObject = new JSONObject(result);
                    success = userObject.getInt("success");
                    if (success == 0) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        Intent intent = new Intent();
                        intent.putExtra(key, values);
                        setResult(resultCode, intent);
                        dialog.dismiss();
                        MyToast.showToast(ChangeMyData.this, "修改成功",R.mipmap.right, Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        dialog.dismiss();
                        MyToast.showToast(ChangeMyData.this, "消息有误请重试",R.mipmap.error, Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                dialog.dismiss();
                MyToast.showToast(ChangeMyData.this, "内部网络错误请稍后重试",R.mipmap.error, Toast.LENGTH_SHORT);
            }
        });
    }
}
