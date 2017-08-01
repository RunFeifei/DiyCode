package com.feifei.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.feifei.common.MultiApplication;


public class AppInfo {

    private static final String META_CLIENT_TYPE = "client_type";
    private static final String META_CHANNEL_NAME = "channel_name";
    private static final String META_UNKNOWN = "unknown";


    /*******************************package Info*******************************/

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.e("AppInfo",e.getMessage());
        }
        return null;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }


    /*******************************meta Info*******************************/

    public static Bundle getMetaData() {
        try {
            return MultiApplication.getContext().getPackageManager().getApplicationInfo(MultiApplication.getContext().getPackageName(), PackageManager
                    .GET_META_DATA)
                    .metaData;
        } catch (NameNotFoundException e) {
            Log.e("AppInfo",e.getMessage());

        }
        return null;
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

    public static String getClientType() {
        return getMetaInfo(META_CLIENT_TYPE);
    }

    public static String getChannelName() {
        return getMetaInfo(META_CHANNEL_NAME);
    }

    /*******************************Build Info*******************************/

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getManufacture() {
        return Build.MANUFACTURER;
    }

    public static String getSystemVersion() {
        return "" + Build.VERSION.SDK_INT;
    }


    /*******************************other Info*******************************/
    /**
     * 获取应用名（manifest中的"label"）
     */
    public static CharSequence getAppName(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
    }

    public static String getImei() {
        String deviceId = null;
        try {
            deviceId = ((TelephonyManager) MultiApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            Log.e("AppInfo",e.getMessage());
        }
        if (TextUtils.isEmpty(deviceId)) {
            return "";
        }
        return deviceId;
    }

}
