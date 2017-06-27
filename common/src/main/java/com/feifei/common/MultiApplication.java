package com.feifei.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class MultiApplication extends MultiDexApplication {

    private static Context context;

    public static void init(Context context) {
        MultiApplication.context = context;
    }

    public static Context getContext() {
        return context;
    }

}
