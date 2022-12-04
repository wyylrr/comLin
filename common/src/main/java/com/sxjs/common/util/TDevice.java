package com.sxjs.common.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.sxjs.common.base.BaseApplication;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

public class TDevice {
    /**
     * Change SP to PX
     *
     * @param resources Resources
     * @param sp        SP
     * @return PX
     */
    public static float spToPx(Resources resources, float sp) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    /**
     * Change Dip to PX
     *
     * @param resources Resources
     * @param dp        Dip
     * @return PX
     */
    public static float dipToPx(Resources resources, float dp) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static float dp2px(float dp) {
        return dp * getDisplayMetrics().density;
    }

    public static int dip2px(float dpValue) {
        float scale = getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static float px2dp(float px) {
        return px / getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return BaseApplication.context.getResources().getDisplayMetrics();
    }

    public static float getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static float getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }

    public static boolean isPortrait() {
        return BaseApplication.context.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 打开或关闭键盘
     */
    public static void startOrCloseKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void closeKeyboard(EditText view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void openKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 平板电脑?
     *
     * @return ??
     */
    public static boolean isTablet() {
        int s = BaseApplication.context.getResources().getConfiguration().screenLayout;
        s &= Configuration.SCREENLAYOUT_SIZE_MASK;
        return s >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void hideSoftKeyboard(View view) {
        if (view == null) return;
        View mFocusView = view;

        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            Activity activity = ((Activity) context);
            mFocusView = activity.getCurrentFocus();
        }
        if (mFocusView == null) return;
        mFocusView.clearFocus();
        InputMethodManager manager = (InputMethodManager) mFocusView.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(mFocusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftKeyboard(View view) {
        if (view == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        if (!view.isFocused()) view.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void gotoMarket(Context context, String pck) {
        if (!isHaveMarket(context)) {
            BaseApplication.showToast("你手机中没有安装应用市场！");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + pck));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean isHaveMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MARKET);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        return infos.size() > 0;
    }

    public static void openAppInMarket(Context context) {
        if (context == null) return;
        String pckName = context.getPackageName();
        gotoMarket(context, pckName);
    }

    public static int getVersionCode() {
        return getVersionCode(BaseApplication.context.getPackageName());
    }

    public static int getVersionCode(String packageName) {
        try {
            return BaseApplication.context
                    .getPackageManager()
                    .getPackageInfo(packageName, 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            return 0;
        }
    }

    public static String getVersionName() {
        try {
            return BaseApplication
                    .context
                    .getPackageManager()
                    .getPackageInfo(BaseApplication.context.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            return "undefined version name";
        }
    }

    public static void installAPK(Context context, File file) {
        if (file == null || !file.exists())
            return;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean openAppActivity(Context context,
                                          String packageName,
                                          String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public static boolean isWifiOpen() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication
                .context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        if (!info.isAvailable() || !info.isConnected()) return false;
        if (info.getType() != ConnectivityManager.TYPE_WIFI) return false;
        return true;
    }


    public static String WIFIName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    public static String BSSIDName(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    public static void copyTextToBoard(String string) {
        if (TextUtils.isEmpty(string)) return;
        ClipboardManager clip = (ClipboardManager) BaseApplication.context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(string);
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * Returns a themed color integer associated with a particular resource ID.
     * If the resource holds a complex {@link ColorStateList}, then the default
     * color from the set is returned.
     *
     * @param resources Resources
     * @param id        The desired resource identifier, as generated by the aapt
     *                  tool. This integer encodes the package, type, and resource
     *                  entry. The value 0 is an invalid identifier.
     * @return A single color value in the form 0xAARRGGBB.
     */
    public static int getColor(Resources resources, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(id, null);
        } else {
            return resources.getColor(id);
        }
    }



    /**
     * The IMEI: 仅仅只对Android手机有效
     * 采用此种方法，需要在AndroidManifest.xml中加入一个许可：android.permission.READ_PHONE_STATE，并且用
     * 户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356（除非你有一个没有量产的手
     * 机（水货）它可能有无效的IMEI，如：0000000000000）。
     *
     * @return imei
     */
    public static String getIMEI1(Context mContext) {
        return getIMEI();
    }

    public static String getIMEI() {
        String serial = null;

        String m_szDevIDShort = "46" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return (new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString()).replaceAll("-", "");
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "shihang"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return (new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString()).replaceAll("-", "");
    }

    public static String getIMEINo() {
        String var3 = Build.SERIAL;
        String var4 = Build.BOARD;
        String var5 = Build.BRAND;
        String var6 = Build.CPU_ABI;
        String var7 = Build.DEVICE;
        String var8 = Build.DISPLAY;
        String var9 = Build.HOST;
        String var10 = Build.ID;
        String var11 = Build.MANUFACTURER;
        String var12 = Build.MODEL;
        String var13 = Build.PRODUCT;
        String var14 = Build.TAGS;
        String var15 = Build.TYPE;
        String var16 = Build.USER;
        String var17 = "35" + var4.length() % 10 + var5.length() % 10 + var6.length() % 10 + var7.length() % 10 + var8.length() % 10 + var9.length() % 10 + var10.length() % 10 + var11.length() % 10 + var12.length() % 10 + var13.length() % 10 + var14.length() % 10 + var15.length() % 10 + var16.length() % 10;
        if (var3 == null) {
            var3 = "zhuge.serial";
        }
        return (new UUID((long) var17.hashCode(), (long) var3.hashCode())).toString();
    }

    public static int isWifiConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) BaseApplication
                    .context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkINfo = cm.getActiveNetworkInfo();
            if (networkINfo != null
                    && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return 1;
        }

    }


    public static String getIP(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return "127.0.0.1";
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return "127.0.0.1";
        }
        if (networkinfo.getType() == ConnectivityManager.TYPE_MOBILE
                && networkinfo.getState() == State.CONNECTED) {// 3g网络成功连接
            String ip = "127.0.0.1";
            try {
                ip = get3GIpAddress();
            } catch (Exception e) {
                ip = "127.0.0.1";
            }
            return ip;
        }
        if (networkinfo.getType() == ConnectivityManager.TYPE_WIFI
                && networkinfo.getState() == State.CONNECTED) {// 3g网络成功连接
            String ip = "127.0.0.1";
            try {
                ip = getWifiIpAddress(context);
            } catch (Exception e) {
                ip = "127.0.0.1";
            }
            return ip;
        }
        return "127.0.0.1";
    }

    @SuppressLint("DefaultLocale")
    @SuppressWarnings("static-access")
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        // 返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
    }

    public static String get3GIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideInputFromWindow(Activity activity) {
        if (activity == null) return;
        InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (inputMethodManager != null) {
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getDate(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    public static String getTime(Long date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getDate(Long date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
