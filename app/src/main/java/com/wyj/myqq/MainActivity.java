package com.wyj.myqq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wyj.myqq.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {

    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button) findViewById(R.id.btn_start);
        RongIM.connect("3lMA6kcEzfuA0nN4FEFrXWdMnPR" +
                "q6EcALVvoO0pOLHYxUZhlonS0W7xYCqkLQaLg8OA66uom3PLrpeCTbLWLhQ==", new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

            }

            @Override
            public void onSuccess(String s) {
                Log.e("main","success");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("main","fail");
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(MainActivity.this, "001","hello");
                }
            }
        });
    }
}
