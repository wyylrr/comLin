package com.hfpq.appservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.sxjs.common.base.BaseActivity;
import com.sxjs.common.service.Defaults;
import com.sxjs.common.service.HttpServer;

public class MainActivity extends BaseActivity {
    private Intent intent = null;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToast();
        showCenterToast("ceshiceshi");
//        Start();
    }

    private void showToast() {
        ToastUtils.delayedShow("测试测试", 4000);
//        ToastUtils.showLong("测试测试");
       /* // 显示 Toast
        ToastUtils.show(CharSequence text);
        ToastUtils.show(int id);
        ToastUtils.show(Object object);

// debug 模式下显示 Toast
        ToastUtils.debugShow(CharSequence text);
        ToastUtils.debugShow(int id);
        ToastUtils.debugShow(Object object);

// 延迟显示 Toast
        ToastUtils.delayedShow(CharSequence text, long delayMillis);
        ToastUtils.delayedShow(int id, long delayMillis);
        ToastUtils.delayedShow(Object object, long delayMillis);

// 显示短 Toast
        ToastUtils.showShort(CharSequence text);
        ToastUtils.showShort(int id);
        ToastUtils.showShort(Object object);

// 显示长 Toast
        ToastUtils.showLong(CharSequence text);
        ToastUtils.showLong(int id);
        ToastUtils.showLong(Object object);

// 自定义显示 Toast
        ToastUtils.show(ToastParams params);

// 取消 Toast
        ToastUtils.cancel();

// 设置 Toast 布局（全局生效）
        ToastUtils.setView(int id);

// 设置 Toast 布局样式（全局生效）
        ToastUtils.setStyle(IToastStyle<?> style);
// 获取 Toast 布局样式
        ToastUtils.getStyle()

// 判断当前框架是否已经初始化
        ToastUtils.isInit();

// 设置 Toast 策略（全局生效）
        ToastUtils.setStrategy(IToastStrategy strategy);
// 获取 Toast 策略
        ToastUtils.getStrategy();

// 设置 Toast 重心和偏移
        ToastUtils.setGravity(int gravity);
        ToastUtils.setGravity(int gravity, int xOffset, int yOffset);

// 设置 Toast 拦截器（全局生效）
        ToastUtils.setInterceptor(IToastInterceptor interceptor);
// 获取 Toast 拦截器
        ToastUtils.getInterceptor();*/
    }

    private void Start() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.http.data");
        registerReceiver(broadcastReceiver, intentFilter);
        InitSetting();
        StartListen();
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showCenterToast("tt");
            Log.e("wyy",intent.getStringExtra("data"));
        }
    };



    private void InitSetting() {
        Defaults.setPort(8089);
        Defaults.setRoot(HttpServer.getLocalIpAddress());
    }

    private void StartListen() {
        HttpServer.Start(this);
    }
    private void Stop() {
        Context context = getApplicationContext();
        HttpServer.Stop(context);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Start();
    }

    public void onClick(View view) {
        tv1.setText("");
    }
}