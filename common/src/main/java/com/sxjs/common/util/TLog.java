package com.sxjs.common.util;

import android.text.TextUtils;
import android.util.Log;

import com.sxjs.common.BuildConfig;


/**
 * Created by yjy on 17/5/25.
 */

public class TLog {
    private static final String LOG_TAG = "ShiHangLog";
//    private static boolean DEBUG = true;
    private static boolean DEBUG = BuildConfig.DEBUG;
    private TLog() {
    }


    public static void debug(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(log)) Log.e(tag, "" + log);
    }


    public static void error(String log) {
        if (DEBUG && !TextUtils.isEmpty(log)) Log.e(LOG_TAG, "" + log);
    }

    public static void log(String log) {
        if (DEBUG && !TextUtils.isEmpty(log)) Log.i(LOG_TAG, log);
    }

    public static void log(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log)) Log.i(tag, log);
    }

    public static void d(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log)) Log.d(tag, log);
    }

    public static void e(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log)) Log.e(tag, log);
    }

    public static void i(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log)) Log.i(tag, log);
    }
}
