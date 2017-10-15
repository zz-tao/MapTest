package cn.kgc.map.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/10/15.
 * 构建全局信息
 * 一定要在 manifest 文件中注册
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 百度地图各组件的信息
        SDKInitializer.initialize(getApplicationContext());
    }
}
