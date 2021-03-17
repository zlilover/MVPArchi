package com.luding.smartoa.util;

import android.util.Log;

import com.luding.smartoa.BuildConfig;

/**
 * Created by lizhen on 2021/3/17.
 */
public class Logger {

    public static boolean isDebug = BuildConfig.LOG_DEBUG || BuildConfig.debugLog;

    public static void e(String tag, String log) {
        if (isDebug) {
            Log.e(tag, log);
        }
    }

    public static void i(String tag, String log) {
        if (isDebug) {
            Log.i(tag, log);
        }
    }

    public static void v(String tag, String log) {
        if (isDebug) {
            Log.v(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (isDebug) {
            Log.d(tag, log);
        }
    }

}
