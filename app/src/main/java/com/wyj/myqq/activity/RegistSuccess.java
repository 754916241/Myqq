package com.wyj.myqq.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Constant;

public class RegistSuccess extends AppCompatActivity {

    private Button go;
    private TextView tvQqnumber;
    private Bundle bundle;
    private Intent intent;
    private String qqnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_success);
        go = (Button) findViewById(R.id.btn_registSuccess_go);
        tvQqnumber = (TextView) findViewById(R.id.tv_registSuccess_qqnumber);
        bundle = getIntent().getExtras();
        qqnumber = bundle.getString(Constant.KEY_QQNUMBER);
        tvQqnumber.setText(qqnumber);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(RegistSuccess.this,Login.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_QQNUMBER, tvQqnumber.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
