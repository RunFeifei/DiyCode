package com.feifei.common.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.feifei.common.MultiApplication;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class PreferenceUtil {

    private static String slUUID;

    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getAppPreferences() {
        return getSharedPreferences(MultiApplication.getContext(), MultiApplication.getContext().getPackageName());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Context context, String key) {
        if (context == null || Strings.isEmpty(key)) {
            return null;
        }
        T t = null;
        String fileName = Base64Utils.encodeToString(key.getBytes());
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            t = (T) ois.readObject();
        } catch (Exception e) {
            context.deleteFile(fileName);
            Log.logStackTrace(e);
        } finally {
            closeStream(ois, fis);
        }
        return t;
    }

    public static void removeObject(Context context, String key) {
        if (context == null || Strings.isEmpty(key)) {
            return;
        }
        String fileName = Base64Utils.encodeToString(key.getBytes());
        context.deleteFile(fileName);
    }

    public static void putObject(Context context, String key, Object object) {
        if (context == null || Strings.isEmpty(key)) {
            return;
        }
        if (object == null) {
            removeObject(context, key);
            return;
        }
        String fileName = Base64Utils.encodeToString(key.getBytes());
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (IOException e) {
            Log.logStackTrace(e);
            context.deleteFile(fileName);
        } finally {
            closeStream(oos, fos);
        }
    }

    private static void closeStream(Closeable... streams) {
        if (streams != null) {
            for (Closeable stream : streams) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.logStackTrace(e);
                    }
                }
            }
        }
    }

    public static String getSlUUID() {
        if (slUUID != null) {
            return slUUID;
        }

        SharedPreferences sp = PreferenceUtil.getAppPreferences();
        slUUID = sp.getString("sl_uuid", null);

        if (slUUID == null) {
            slUUID = UUID.randomUUID().toString();
            sp.edit().putString("sl_uuid", slUUID).commit();
        }

        return slUUID;
    }

}
