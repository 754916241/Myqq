package com.wyj.myqq.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.wyj.myqq.R;
import com.wyj.myqq.adapter.LocationAdapter;
import com.wyj.myqq.bean.AddressBean;
import com.wyj.myqq.utils.Config;
import com.wyj.myqq.utils.Constant;
import com.wyj.myqq.view.MyToast;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.LocationMessage;

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
    private ListView lvAddress;
    private ArrayList<AddressBean> addressBean;

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
        addressBean = new ArrayList<>();
        conversationType = (Conversation.ConversationType) bundle.getSerializable(Constant.KEY_CONVERSATION_TYPE);
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        mapView = (MapView) findViewById(R.id.map);
        lvAddress = (ListView) findViewById(R.id.lv_address);
        tvRight.setOnClickListener(this);
        tvTitle.setText("地理位置");
        tvRight.setText("发送");
    }

    private void initMap() {
        if(aMap == null){
            aMap = mapView.getMap();
        }
        aMap.showIndoorMap(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        locationStyle = new MyLocationStyle();
        //定位一次，且将视角移动到地图中心点。
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        //locationStyle.interval(2000);
        aMap.setMyLocationStyle(locationStyle);
        aMap.setMyLocationEnabled(true);


    }

    /**
     * 不连续定位，每次点击只获取一次定位结果并显示在界面上
     */
    private void initLocation() {
        locationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        //获取一次定位结果
        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果
        option.setOnceLocationLatest(true);

        locationClient.setLocationOption(option);
        AMapLocationListener listener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null &&  aMapLocation.getErrorCode() == 0) {

                    lat = aMapLocation.getLatitude();
                    lng = aMapLocation.getLongitude();
                    String description = aMapLocation.getProvince() + aMapLocation.getCity() +
                            aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
                    poi = description + aMapLocation.getPoiName();
                    imgUri = getMapUrl(lat, lng);
                    addressBean.add(new AddressBean("位置信息",description + aMapLocation.getPoiName()));
                    addressBean.add(new AddressBean("住宅信息",description + aMapLocation.getAoiName()));
                    lvAddress.setAdapter(new LocationAdapter(SendLocation.this,R.layout.item_address_list,addressBean));
                    Log.d("SENDLOCATION", "poi信息为" + poi);
                }else{
                        MyToast.showToast(SendLocation.this,aMapLocation.getErrorInfo()+",code is"+aMapLocation.getErrorCode(), Toast.LENGTH_SHORT);
                }
            }

        };
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                poi = addressBean.get(i).getAddress();
                Log.d("SENDLOCATION", "poi信息(拼接后的地址)为" + poi);
            }
        });
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
        locationClient.onDestroy();
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
