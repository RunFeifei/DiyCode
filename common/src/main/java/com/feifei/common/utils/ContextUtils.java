package com.feifei.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.feifei.common.MultiApplication;


public class ContextUtils {
    // 以此channel标记GooglePlay应用
    private static final String CHANNEL_GOOGLE = "play.google.com";
    private static final String CHANNEL_UMENG = "UMENG_CHANNEL";

    private static final String PREFERENCES_NAME = "CONTEXT";
    private static final String PREF_KEY_DEVICE_TOKEN = "deviceToken";

    private static final String META_CLIENT_TYPE = "client_type";
    private static final String META_APP_NAME = "app_name";
    private static final String META_CHANNEL_NAME = "channel_name";
    private static final String META_UNKNOWN = "unknown";

    private static int autoAccRequestCode = 10001;
    private static int autoAccResultCode = 10001;

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.logStackTrace(e);
        }
        return null;
    }

    /**
     * 获取应用名（manifest中定义的"label"）
     * @param context
     * @return
     */
    public static CharSequence getAppLabel(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
    }

    public static int getAutoAccRequestCode() {
        return autoAccRequestCode++;
    }

    public static int getAutoAccResultCode() {
        return autoAccResultCode++;
    }

    public static Bundle getMetaData() {
        try {
            return MultiApplication.getContext().getPackageManager().getApplicationInfo(MultiApplication.getContext().getPackageName(), PackageManager.GET_META_DATA)
                    .metaData;
        } catch (NameNotFoundException e) {
            Log.logStackTrace(e);
        }
        return null;
    }

    public static String getClientType() {
        return getMetaInfo(META_CLIENT_TYPE);
    }

    /**
     * 获取manifest中定义的APP_NAME，用于服务器识别不同的app。
     * @return
     */
    public static String getAppName() {
        return getMetaInfo(META_APP_NAME);
    }

    public static String getReferralName() {
        return getMetaInfo("REFERRER_NAME");
    }

    /**
     * Not recommanded
     *
     * @return
     */
    public static String getUmengChannel() {
        return getMetaInfo(CHANNEL_UMENG);
    }

    public static String getChannelName() {
        return getMetaInfo(META_CHANNEL_NAME);
    }

    public static String getMetaInfo(String key) {
        if (getMetaData() == null) {
            return META_UNKNOWN;
        }
        String metaInfo = getMetaData().getString(key);
        if (metaInfo == null) {
            metaInfo = META_UNKNOWN;
        }
        return metaInfo;
    }

    public static boolean isGoogleApp() {
        return CHANNEL_GOOGLE.equals(getChannelName());
    }

    public static void showSystemKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSystemKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return "" + Build.VERSION.SDK_INT;
    }

    public static void saveDeviceToken(String token) {
        SharedPreferences sp = PreferenceUtil.getSharedPreferences(MultiApplication.getContext(), PREFERENCES_NAME);
        sp.edit().putString(PREF_KEY_DEVICE_TOKEN, token).commit();
    }

    public static String getDeviceToken() {
        SharedPreferences sp = PreferenceUtil.getSharedPreferences(MultiApplication.getContext(), PREFERENCES_NAME);
        return sp.getString(PREF_KEY_DEVICE_TOKEN, "");
    }

    public static String getAndroidId() {
        String androidId = null;
        try {
            androidId = Settings.System.getString(MultiApplication.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            Log.logStackTrace(e);
        }
        if (TextUtils.isEmpty(androidId)) {
            return "";
        }
        return androidId;
    }

    public static String getImei() {
        String deviceId = null;
        try {
            deviceId = ((TelephonyManager) MultiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            Log.logStackTrace(e);
        }
        if (TextUtils.isEmpty(deviceId)) {
            return "";
        }
        return deviceId;
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
