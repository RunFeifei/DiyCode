package com.example.root.okfit;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.CrashHandler;
import com.dianrong.android.common.utils.Log;
import com.dianrong.crnetwork.error.DrErrorMsgHelper;
import com.dianrong.crnetwork.host.BaseUrlBindHelper;
import com.dianrong.crnetwork.host.ServerType;
import com.dianrong.crnetwork.host.dianrong.hosts.DianRongHosts;

import java.io.File;
import java.io.IOException;

/**
 * Created by PengFeifei on 17-4-19.
 */

public class OKFitApplication extends AppContext {

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
        BaseUrlBindHelper.resetBaseUrl(ServerType.PRODUCT);
        BaseUrlBindHelper.initBaseUrl(DianRongHosts.PRODUCT);
        DrErrorMsgHelper.initErrorMsgs();
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
