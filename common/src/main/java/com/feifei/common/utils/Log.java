package com.feifei.common.utils;


import com.feifei.common.BuildConfig;

/**
 * 用三方库替换!!!!!!!!!!!!!
 */
public class Log {


    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, tr);
        }
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        android.util.Log.e(tag, msg, tr);
    }

    public static void logStackTrace(Exception e) {
        if (!BuildConfig.DEBUG || e == null) {
            return;
        }
        StringBuilder buf = new StringBuilder();
        buf.append(e.toString());
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (StackTraceElement element : stackTraceElements) {
            buf.append("\n\t\t\t\t");
            buf.append(element.toString());
        }
        android.util.Log.w("logStackTrace", buf.toString());
    }
}
