package com.sxjs.common.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.sxjs.common.widget.percentlayout.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class AppUtil {

    public static final int INSTALL_PERMISS_CODE = 321;

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return "1.1.0";
    }

    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 静默安装
     *
     * @param apkPath
     * @return
     */
    public static void installClientApp(String apkPath) {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            // 7.0以后版本需要额外添加
            //           "-i", "当前应用包名",
            // 两个字段，并且需要应用支持 android.permission.INSTALL_PACKAGES 权限**
            process = new ProcessBuilder("pm", "install", "-i", BuildConfig.APPLICATION_ID, "-r", apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception " + e.toString());
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {

            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.e(TAG, "errorMsg " + errorMsg.toString());
        Log.d(TAG, "successMsg " + successMsg.toString());
    }

    /**
     * 判断某个App是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    public static String getAppPackageName(Context context) {
        return context.getApplicationInfo().packageName;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apk
     */
    public static void installApp(Activity context, File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = getAppPackageName(context) + ".fileprovider";
            Uri apkUri = FileProvider.getUriForFile(context, authority, apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(apk);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public static void doSetLocalTime(Context mContext, long time) {
//        boolean is24Hour = DateFormat.is24HourFormat(mContext);
//        if (!is24Hour) {
//            android.provider.Settings.System.putString(mContext.getContentResolver(),
//                    android.provider.Settings.System.TIME_12_24, "24");
//        }
//        try {
//            boolean isAUTO_TIME_ZONE = android.provider.Settings.Global.getInt(mContext.getContentResolver(),
//                    android.provider.Settings.Global.AUTO_TIME_ZONE) > 0;
//            if (isAUTO_TIME_ZONE) {
//                android.provider.Settings.Global.putInt(mContext.getContentResolver(),
//                        android.provider.Settings.Global.AUTO_TIME_ZONE, 0);
//            }
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            boolean AUTO_TIME = android.provider.Settings.Global.getInt(mContext.getContentResolver(),
//                    android.provider.Settings.Global.AUTO_TIME) > 0;
//            if (AUTO_TIME) {
//                android.provider.Settings.Global.putInt(mContext.getContentResolver(),
//                        android.provider.Settings.Global.AUTO_TIME, 0);
//            }
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (time < Integer.MAX_VALUE) {
//            ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTimeZone("GMT+08:00");
//            ((AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE)).setTime(time);
//        }
        SystemClock.setCurrentTimeMillis(time);
    }

    public static String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式


        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08")); //设置时区


        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间


        String createDate = formatter.format(curDate);   //格式转换

        return createDate;
    }
}
