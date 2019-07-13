package com.wei.cookbook.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.wei.cookbook.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;


public class LocationActivity extends AppCompatActivity implements  LocationSource, AMapLocationListener{
        private MapView mMapView = null;
        private AMap aMap = null;
        private OnLocationChangedListener mListener = null;
        private AMapLocationClient mLocationClient = null;
        private AMapLocationClientOption locationOption = new AMapLocationClientOption();
        private boolean isFirstLoc = true;
        private int ACCESS_COARSE_LOCATION_CODE = 1;
        private int ACCESS_FINE_LOCATION_CODE = 2;
        private int WRITE_EXTERNAL_STORAGE_CODE = 3;
        private int READ_EXTERNAL_STORAGE_CODE = 4;
        private int READ_PHONE_STATE_CODE = 5;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_location);
                Button mButton = findViewById(R.id.poi_search);
                ButterKnife.bind(this);
                mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(LocationActivity.this,PoiSearchActivity.class );
                                startActivity(intent);
                        }
                });
                //获取地图控件引用
                mMapView = (MapView) findViewById(R.id.map);
                //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
                mMapView.onCreate(savedInstanceState);
//
//                //SDK在Android 6.0下需要进行运行检测的权限如下：
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        //申请WRITE_EXTERNAL_STORAGE权限
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_CODE);
//                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
//                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
//                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
//                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
//                }

                if (aMap == null) {
                        aMap = mMapView.getMap();
                        //设置显示定位按钮 并且可以点击
                        UiSettings settings = aMap.getUiSettings();
                        aMap.setLocationSource(this);//设置了定位的监听,这里要实现LocationSource接口
                        // 是否显示定位按钮
                        settings.setMyLocationButtonEnabled(true);
                        aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase
                }

                //初始化定位
                mLocationClient = new AMapLocationClient(getApplicationContext());
                //设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
                mLocationClient.setLocationListener(this);
                //初始化定位参数
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                //设置是否只定位一次,默认为false
                mLocationOption.setOnceLocation(false);
                //设置是否强制刷新WIFI，默认为强制刷新
                mLocationOption.setWifiActiveScan(true);
                //设置是否允许模拟位置,默认为false，不允许模拟位置
                mLocationOption.setMockEnable(false);
                //设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);
                //给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
                //启动定位
                mLocationClient.startLocation();

        }
        @Override
        protected void onDestroy() {
                super.onDestroy();
                //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
                mMapView.onDestroy();
                mLocationClient.stopLocation();//停止定位
                mLocationClient.onDestroy();//销毁定位客户端。
                //销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。
        }
        @Override
        protected void onResume() {
                super.onResume();
                //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
                mMapView.onResume();
        }
        @Override
        protected void onPause() {
                super.onPause();
                //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
                mMapView.onPause();
        }
        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
                mMapView.onSaveInstanceState(outState);
        }
        //激活定位
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
        }

        @Override
        public void deactivate() {
                mListener = null;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                                //定位成功回调信息，设置相关消息
                                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                                double latitude = aMapLocation.getLatitude();//获取纬度
                                double longitude = aMapLocation.getLongitude();//获取经度
                                aMapLocation.getAccuracy();//获取精度信息
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date(aMapLocation.getTime());
                                df.format(date);//定位时间
                                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                                aMapLocation.getCountry();//国家信息
                                aMapLocation.getProvince();//省信息
                                aMapLocation.getCity();//城市信息
                                aMapLocation.getDistrict();//城区信息
                                aMapLocation.getStreet();//街道信息
                                aMapLocation.getStreetNum();//街道门牌号信息
                                aMapLocation.getCityCode();//城市编码
                                aMapLocation.getAdCode();//地区编码

                                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                                if (isFirstLoc) {
                                        //设置缩放级别
                                        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                                        //将地图移动到定位点
                                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                                        //点击定位按钮 能够将地图的中心移动到定位点
                                        mListener.onLocationChanged(aMapLocation);
                                        //获取定位信息
                                        StringBuffer buffer = new StringBuffer();
                                        buffer.append(aMapLocation.getCountry() + ""
                                                + aMapLocation.getProvince() + ""
                                                + aMapLocation.getCity() + ""
                                                + aMapLocation.getProvince() + ""
                                                + aMapLocation.getDistrict() + ""
                                                + aMapLocation.getStreet() + ""
                                                + aMapLocation.getStreetNum());
                                        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                                        isFirstLoc = false;
                                }
                        } else {
                                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                                Log.e("AmapError", "location Error, ErrCode:"
                                        + aMapLocation.getErrorCode() + ", errInfo:"
                                        + aMapLocation.getErrorInfo());
                                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                        }
                }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                //可在此继续其他操作。
                if (requestCode == ACCESS_COARSE_LOCATION_CODE){
                        Toast.makeText(getApplicationContext(), "ACCESS_COARSE_LOCATION_CODE", Toast.LENGTH_LONG).show();
                } else if (requestCode == ACCESS_FINE_LOCATION_CODE){
                        Toast.makeText(getApplicationContext(), "ACCESS_FINE_LOCATION_CODE", Toast.LENGTH_LONG).show();
                } else if (requestCode == WRITE_EXTERNAL_STORAGE_CODE){
                        Toast.makeText(getApplicationContext(), "WRITE_EXTERNAL_STORAGE_CODE", Toast.LENGTH_LONG).show();
                } else if (requestCode == READ_EXTERNAL_STORAGE_CODE){
                        Toast.makeText(getApplicationContext(), "READ_EXTERNAL_STORAGE_CODE", Toast.LENGTH_LONG).show();
                } else if (requestCode == READ_PHONE_STATE_CODE){
                        Toast.makeText(getApplicationContext(), "READ_PHONE_STATE_CODE", Toast.LENGTH_LONG).show();
                }

        }


}
