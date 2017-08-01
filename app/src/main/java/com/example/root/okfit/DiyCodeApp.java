package com.example.root.okfit;

import android.content.Context;
import android.text.TextUtils;

import com.dianrong.crnetwork.host.BaseUrlBinder;
import com.dianrong.crnetwork.host.ServerType;
import com.dianrong.crnetwork.request.ClientBuilder;
import com.example.root.okfit.net.bean.Token;
import com.example.root.okfit.util.TokenHelper;
import com.feifei.common.MultiApplication;
import com.feifei.common.utils.Log;

import java.io.File;
import java.io.IOException;

import static com.example.root.okfit.util.UserManager.KEY_TOKEN;
import static com.example.root.okfit.util.UserManager.TOKEN_PREFIX;

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
        BaseUrlBinder.resetBaseUrl(ServerType.PRODUCT);
        BaseUrlBinder.initBaseUrl("https://diycode.cc/api/v3/");
        syncToken(this);
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

    private static void syncToken(Context context) {
        Token token = new TokenHelper(context).getToken();
        if (token == null) {
            return;
        }
        String accessToken = token.getAccess_token();
        if (TextUtils.isEmpty(accessToken)) {
            return;
        }
        accessToken = TOKEN_PREFIX + accessToken;
        ClientBuilder.putHeader(KEY_TOKEN, accessToken);
    }

}
