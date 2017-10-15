package cn.kgc.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity implements BDLocationListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    /**
     * 是否是第一次定位
     */
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取地图控件
        mMapView = (MapView) findViewById(R.id.map_container);

        location();
    }

    private void location() {
        // 获取百度地图对象
        mBaiduMap = mMapView.getMap();

        // 启用定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 初始化定位的基本信息
        LocationClient mLocationClient = new LocationClient(this);
        // 注册定位监听器
        mLocationClient.registerLocationListener(this);


        // 定位参数
        LocationClientOption option = new LocationClientOption();
        // 打开 gps
        option.setOpenGps(true);
        // 扫描定位次数 默认定位一次
        option.setScanSpan(1000);
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        // 当 activity 销毁 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 当接收到定位信息时回调
     *
     * @param location
     */
    @Override
    public void onReceiveLocation(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null) {
            return;
        }

        // 构建当前的位置信息
        MyLocationData locData = new MyLocationData.Builder()
                // 设置旋转角度
                .accuracy(location.getRadius())
                // 设置当前位置的经纬度信息
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();

        // 设置当前的位置信息
        mBaiduMap.setMyLocationData(locData);

        if (isFirstLoc) {
            // 修改定位状态
            isFirstLoc = false;
            // 构建定位的信息
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);

            // 运动到当前位置
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }
}
