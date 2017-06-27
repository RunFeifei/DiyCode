package com.example.root.okfit;

import com.dianrong.crnetwork.host.BaseUrlBinder;
import com.dianrong.crnetwork.host.ServerType;
import com.feifei.common.MultiApplication;
import com.feifei.common.CrashHandler;
import com.feifei.common.utils.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class DiyCodeApp extends MultiApplication {

    public static String cacheDir = null;

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
        initCacheDir();
        try {
            CrashHandler.init(this.getApplicationContext());
        } catch (Exception ex) {
            Log.e("-->", "Cannot initialize the CrashHandler.", ex);
        }
        BaseUrlBinder.resetBaseUrl(ServerType.PRODUCT);
        BaseUrlBinder.initBaseUrl("https://diycode.cc/api/v3/");
    }


    private void initCacheDir() {

        File cacheFile = getExternalCacheDir();
        if (cacheFile != null) {
            try {
                cacheDir = cacheFile.getCanonicalPath();
            } catch (IOException e) {
                Log.logStackTrace(e);
            }
        }

        if (cacheDir == null) {
            try {
                cacheDir = getCacheDir().getCanonicalPath();
            } catch (IOException e) {
                Log.logStackTrace(e);
                System.exit(1);
            }
        }
    }

}
