package com.wyj.myqq.activity;

import android.content.Context;
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
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

public class Suggest extends AppCompatActivity {

    private TextView tvBack, tvTitle;
    private String qqnumber;
    private EditText edtSuggest;
    private Button btnSubmit;
    private ImageView imgLeft;
    private InputMethodManager imm;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        initData();
        initView();
        initClick();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSuggest.getText().toString().equals("")){
                    MyToast.showToast(Suggest.this,"请您填写对我们的意见", Toast.LENGTH_SHORT);
                }
                MyToast.showToast(Suggest.this,"提交成功，我们会认真考虑您的建议，谢谢！",R.mipmap.right, Toast.LENGTH_SHORT);
                setResult(Constant.RESULT_CODE_SUGGEST);
                finish();

            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(edtSuggest.getWindowToken(), 0);
                onBackPressed();
            }
        });
    }

    private void initView() {
        imgLeft = (ImageView) findViewById(R.id.img_left);
        tvTitle = (TextView) findViewById(R.id.title);
        edtSuggest = (EditText) findViewById(R.id.edt_suggest);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        tvTitle.setText("意见反馈");
        imgLeft.setVisibility(View.VISIBLE);
    }
}
