package com.dianrong.crnetwork;

import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.utils.Log;
import com.dianrong.android.common.utils.UserStorageUtils;
import com.dianrong.crnetwork.cookie.CrCookiePersistor;
import com.example.crnetwork.BuildConfig;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import util.Collections;
import util.Strings;

/**
 * 适配Okhttp3.0
 * Created by yangcheng on 15/12/25.
 */
public class CrOkCookieStore extends PersistentCookieJar {

    private static CrOkCookieStore instance;
    private static SetCookieCache setCookieCache = new SetCookieCache();
    private static CrCookiePersistor crCookiePersistor = new CrCookiePersistor();

    private static final String DOMAINS_PREFERENCES = "domains";
    private static CookieManager cookieManager;
    @SuppressWarnings("deprecation")
    private static CookieSyncManager cookieSyncManager;

    public static CrOkCookieStore getInstance() {
        if (instance == null) {
            cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                cookieSyncManager = CookieSyncManager.createInstance(AppContext.getInstance());
            }
            instance = new CrOkCookieStore(setCookieCache, crCookiePersistor);
        }
        return instance;
    }

    public CrOkCookieStore(CookieCache cache, CookiePersistor persistor) {
        super(cache, persistor);
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        setCookieCache.addAll(cookies);
        crCookiePersistor.saveAll(cookies);
        syncToManager(cookies);
    }

    private void syncToManager(List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            cookieManager.setCookie(cookie.domain(), cookie.toString());
            String domains = UserStorageUtils.getDRPreferences().getString(CrOkCookieStore.DOMAINS_PREFERENCES, null);
            if (Strings.isEmpty(domains)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cookieManager.flush();
                } else {
                    cookieSyncManager.sync();
                }
                return;
            }
            Gson gson = new Gson();
            Set<String> set = gson.fromJson(domains, new TypeToken<HashSet<String>>() {
            }.getType());
            if (Collections.isEmpty(set)) {
                return;
            }
            // 将sessionId保存到slSessionId
            for (String domain : set) {
                if (cookie.name().equals("JSESSIONID")) {
                    cookieManager.setCookie(domain, "slSessionId=" + cookie.value());
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            cookieSyncManager.sync();
        }
    }


    public boolean checkWebCookieUpdate(String url) {
        String domain = url.split("//", 2)[1];
        if (Strings.isEmpty(domain)) {
            return false;
        }
        SharedPreferences sharedPreferences = UserStorageUtils.getDRPreferences();
        String domains = sharedPreferences.getString(CrOkCookieStore.DOMAINS_PREFERENCES, null);
        boolean needUpdate = domains == null || !domains.contains(domain);
        if (!needUpdate) {
            return false;
        }
        Gson gson = new Gson();
        Set<String> set = new HashSet<String>();
        if (TextUtils.isEmpty(domains)) {
            set.add(domain);
            String strJson = gson.toJson(set);
            sharedPreferences.edit().putString(DOMAINS_PREFERENCES, strJson).commit();
            return true;
        }
        set = gson.fromJson(domains, new TypeToken<HashSet<String>>() {
        }.getType());
        if (Collections.isEmpty(set)) {
            set = new HashSet<String>();
        }
        set.add(domain);
        String strJson = gson.toJson(set);
        sharedPreferences.edit().putString(DOMAINS_PREFERENCES, strJson).commit();
        return true;
    }


    public synchronized boolean removeAll() {
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            cookieSyncManager.sync();
        }
        clear();
        return true;
    }

    public void logCookies(String url) {
        if (BuildConfig.DEBUG && instance != null && url != null) {
            Log.d("cookie", "logCookies : " + instance.cookieManager.getCookie(url));
        }
    }

}
