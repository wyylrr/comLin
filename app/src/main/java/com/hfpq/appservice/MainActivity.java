package com.hfpq.appservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sxjs.common.service.Defaults;
import com.sxjs.common.service.HttpServer;

public class MainActivity extends AppCompatActivity {
    private Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Start();
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

}