package com.sxjs.common.base;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.sxjs.common.CommonModule;

public class BaseApplication extends Application {

    public static Context context;

    public static void showToast(String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CommonModule.init(this);
        context = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
