package com.wyj.myqq.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.wyj.myqq.R;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.LocationMessage;

import static com.amap.api.maps.CameraUpdateFactory.zoomTo;

public class SendLocation extends AppCompatActivity implements View.OnClickListener {

    /**
     * 取消
     */
    private TextView tvLeft;
    private TextView tvTitle;
    private TextView tvRight;

    //地图信息
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle locationStyle;
    private AMapLocationClient locationClient;

    //位置信息参数
    private double lat,lng;
    private String poi;
    private Uri imgUri;

    private Bundle bundle;
    private String targetId;
    private Conversation.ConversationType conversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        Config.setNotificationBar(this,R.color.colorApp);
        initView();
        initData();
        mapView.onCreate(savedInstanceState);
        initMap();
        initLocation();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        targetId = bundle.getString(Constant.KEY_FRIENDS_QQNUMBER);
        conversationType = (Conversation.ConversationType) bundle.getSerializable(Constant.KEY_CONVERSATION_TYPE);
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        mapView = (MapView) findViewById(R.id.map);
        tvRight.setOnClickListener(this);
        tvTitle.setText("地理位置");
        tvRight.setText("发送");
    }

    private void initMap() {
        if(aMap == null){
            aMap = mapView.getMap();
        }
        aMap.showIndoorMap(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        locationStyle = new MyLocationStyle();
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        locationStyle.interval(2000);
        aMap.setMyLocationStyle(locationStyle);
        aMap.setMyLocationEnabled(true);


    }

    private void initLocation() {
        locationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setNeedAddress(true);
        option.setLocationCacheEnable(true);
        locationClient.setLocationOption(option);
        AMapLocationListener listener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){
                        lat = aMapLocation.getLatitude();
                        lng = aMapLocation.getLongitude();
                        poi = aMapLocation.getAddress();
                        imgUri = getMapUrl(lat,lng);
                    }else{
                        MyToast.showToast(SendLocation.this,aMapLocation.getErrorInfo()+",code is"+aMapLocation.getErrorCode(), Toast.LENGTH_SHORT);
                    }
                }
            }
        };
        locationClient.setLocationListener(listener);
        locationClient.startLocation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.tv_right:
                LocationMessage locationMessage = LocationMessage.obtain(lat,lng,poi,imgUri);
                Message message = Message.obtain(targetId,conversationType,locationMessage);
                RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {

                    }

                    @Override
                    public void onSuccess(Message message) {

                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                    }
                });
                finish();
                break;
        }
    }

    private Uri getMapUrl(double x, double y) {
        String url = "http://restapi.amap.com/v3/staticmap?location=" + y + "," + x +
                "&zoom=17&scale=2&size=150*150&markers=mid,,A:" + y + ","
                + x + "&key=" + "ee95e52bf08006f63fd29bcfbcf21df0";
        return Uri.parse(url);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}
