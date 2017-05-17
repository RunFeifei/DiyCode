package com.dianrong.crnetwork;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.utils.Log;
import com.dianrong.android.common.utils.UserStorageUtils;
import com.example.crnetwork.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import rx.functions.Action2;
import rx.functions.Func2;
import util.Collections;
import util.Strings;

/**
 * 适配Okhttp3.0
 * <p/>
 * changed by ycangcheng on 16/03/25.
 * <p/>
 * Created by yangcheng on 15/12/25.
 * loadForRequest()-->发送时向request header中加入cookie
 * saveFromResponse()-->接收时读取response header中的cookie
 */
public class CrOkCookieStore implements CookieJar {
    private static final String TAG = CrOkCookieStore.class.getSimpleName();
    private static final String DOMAINS_PREFERENCES = "domains";

    private static CrOkCookieStore instance;
    private Map<URI, Set<CookieWrapper>> allCookies;
    private CookieManager cookieManager;
    @SuppressWarnings("deprecation")
    private CookieSyncManager csm;

    private Action2<HttpUrl, List<Cookie>> onSaveListener;
    private Func2<HttpUrl, List<Cookie>, List<Cookie>> onLoadListener;

    @SuppressWarnings("deprecation")
    private CrOkCookieStore() {
        allCookies = new HashMap<>();
        cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            csm = CookieSyncManager.createInstance(AppContext.getInstance());
        }
    }

    public static CrOkCookieStore getInstance() {
        if (instance == null) {
            instance = new CrOkCookieStore();
        }
        return instance;
    }

    private static URI cookieUri(HttpUrl url, Cookie cookie) {
        URI cookieUri = url.uri();
        if (cookie.domain() != null) {

            String domain = cookie.domain();
            if (domain.charAt(0) == '.') {
                domain = domain.substring(1);
            }
            try {
                cookieUri = new URI(url.scheme() == null ? "http" : url.scheme(), domain, cookie.path() == null ? "/" : cookie.path(), null);
            } catch (URISyntaxException e) {
                Log.w(TAG, e);
            }
        }
        return cookieUri;
    }

    public static void logCookies(String url) {
        if (BuildConfig.DEBUG && instance != null && url != null) {
            Log.d("cookie", "logCookies : " + instance.cookieManager.getCookie(url));
        }
    }

    public synchronized List<Cookie> get(URI uri) {
        return getValidCookies(uri);
    }

    public void setOnSaveListener(Action2<HttpUrl, List<Cookie>> onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    public void setOnLoadListener(Func2<HttpUrl, List<Cookie>, List<Cookie>> onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

        if (onSaveListener != null) {
            onSaveListener.call(url, cookies);
        }

        for (Cookie cookie : cookies) {
            URI uri = cookieUri(url, cookie);
            CookieWrapper cw = new CookieWrapper(cookie);

            Set<CookieWrapper> targetCookies = allCookies.get(uri);
            if (targetCookies == null) {
                targetCookies = new HashSet<>();
                allCookies.put(uri, targetCookies);
            }
            targetCookies.remove(cw);
            targetCookies.add(cw);
        }

        syncToManager(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> validCookies = getValidCookies(url.uri());
        if (onLoadListener != null) {
            validCookies = onLoadListener.call(url, validCookies);
        }
        return validCookies;
    }

    @SuppressWarnings("deprecation")
    private void syncToManager(URI uri, Cookie cookie) {
        Log.d("cookie", uri.toString() + ": " + cookie.domain() + ": " + cookie.toString());
        setCookie(cookie);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            csm.sync();
        }
    }

    private void syncToManager(HttpUrl url, List<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            Log.d("cookie", url.toString() + ": " + cookie.domain() + ": " + cookie.toString());
            setCookie(cookie);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            csm.sync();
        }
    }

    private void setCookie(Cookie cookie) {
        cookieManager.setCookie(cookie.domain(), cookie.toString());
        String domains = UserStorageUtils.getDRPreferences().getString(CrOkCookieStore.DOMAINS_PREFERENCES, null);
        if (Strings.isEmpty(domains)) {
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

    static boolean updateCookie(String url) {
        String domain = url.split("//", 2)[1];
        if (Strings.isEmpty(domain)) {
            return false;
        }
        SharedPreferences sharedPreferences = UserStorageUtils.getDRPreferences();
        String domains = sharedPreferences.getString(CrOkCookieStore.DOMAINS_PREFERENCES, null);
        boolean needUpdate = domains != null && !domains.contains(domain);
        if (!needUpdate) {
            return false;
        }
        Set<String> set = new HashSet<String>();
        Gson gson = new Gson();
        set = gson.fromJson(domains, new TypeToken<HashSet<String>>() {
        }.getType());
        if (Collections.isEmpty(set)) {
            set = new HashSet<String>();
        }
        set.add(domain);
        String strJson = gson.toJson(set);
        sharedPreferences.edit().putString(DOMAINS_PREFERENCES, strJson).apply();
        return true;
    }

    @SuppressWarnings("deprecation")
    public synchronized void forceSync2Webkit() {
        if (allCookies == null || allCookies.size() == 0) {
            return;
        }
        for (Map.Entry<URI, Set<CookieWrapper>> entry : allCookies.entrySet()) {
            Set<CookieWrapper> cookieSet = entry.getValue();
            if (cookieSet != null && !cookieSet.isEmpty()) {
                for (CookieWrapper cw : cookieSet) {
                    setCookie(cw.cookie);
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            csm.sync();
        }
    }

    @SuppressWarnings("unused")
    public synchronized List<Cookie> getCookies() {
        List<Cookie> allValidCookies = new ArrayList<>();
        for (URI storedUri : allCookies.keySet()) {
            allValidCookies.addAll(getValidCookies(storedUri));
        }

        return allValidCookies;
    }

    private List<Cookie> getValidCookies(URI uri) {
        List<CookieWrapper> targetCookies = new ArrayList<>();
        // If the stored URI does not have a path then it must match any URI in
        // the same domain
        for (URI storedUri : allCookies.keySet()) {
            // Check ith the domains match according to RFC 6265
            if (checkDomainsMatch(storedUri.getHost(), uri.getHost())) {
                // Check if the paths match according to RFC 6265
                if (checkPathsMatch(storedUri.getPath(), uri.getPath())) {
                    targetCookies.addAll(allCookies.get(storedUri));
                }
            }
        }

        // Check it there are expired cookies and remove them
        if (!targetCookies.isEmpty()) {
            List<CookieWrapper> cookiesToRemoveFromPersistence = new ArrayList<>();
            for (Iterator<CookieWrapper> it = targetCookies.iterator(); it.hasNext(); ) {
                CookieWrapper currentCookie = it.next();
                if (currentCookie.cookie.expiresAt() > 0 && currentCookie.cookie.expiresAt() < System.currentTimeMillis()) {
                    cookiesToRemoveFromPersistence.add(currentCookie);
                    it.remove();
                }
            }

            if (!cookiesToRemoveFromPersistence.isEmpty()) {
                removeFromManager(uri, cookiesToRemoveFromPersistence);
            }
        }

        List<Cookie> result = new ArrayList<>(targetCookies.size());
        for (CookieWrapper cw : targetCookies) {
            result.add(cw.cookie);
        }
        return result;
    }

    private boolean checkDomainsMatch(String cookieHost, String requestHost) {
        return requestHost.equals(cookieHost) || requestHost.endsWith("." + cookieHost);
    }

    private boolean checkPathsMatch(String cookiePath, String requestPath) {
        return requestPath.equals(cookiePath) ||
                (requestPath.startsWith(cookiePath) && cookiePath.charAt(cookiePath.length() - 1) == '/') ||
                (requestPath.startsWith(cookiePath) && requestPath.substring(cookiePath.length()).charAt(0) == '/');
    }

    private void removeFromManager(URI uri, List<CookieWrapper> cookiesToRemove) {
        // TODO nothing
    }

    public synchronized List<URI> getURIs() {
        return new ArrayList<>(allCookies.keySet());
    }

    public synchronized boolean remove(URI uri, Cookie cookie) {
        Set<CookieWrapper> targetCookies = allCookies.get(uri);
        boolean cookieRemoved = targetCookies != null && targetCookies.remove(new CookieWrapper(cookie));
        if (cookieRemoved) {
            removeFromManager(uri, cookie);
        }
        return cookieRemoved;

    }

    private void removeFromManager(URI uri, Cookie cookieToRemove) {
        // TODO nothing
    }

    public synchronized boolean removeAll() {
        allCookies.clear();
        removeAllFromManager();
        return true;
    }

    @SuppressWarnings("deprecation")
    private void removeAllFromManager() {
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            csm.sync();
        }
    }

    public synchronized void syncCookies(Uri destUri, Uri srcUri, String scheme) {
        //    Uri destUri = Uri.parse(UrlBuilder.getBaseUrl());
        HttpUrl httpUrl = new HttpUrl.Builder().scheme(scheme).host(srcUri.getHost()).build();
        String cookieInfo = CookieManager.getInstance().getCookie(srcUri.getHost());
        if (cookieInfo == null) {
            return;
        }
        String[] cookieValues = cookieInfo.split(";");
        List<Cookie> cookieList = new ArrayList<>();
        for (int i = 0; i < cookieValues.length; i++) {
            Cookie cookie = Cookie.parse(httpUrl, cookieValues[i].trim());
            if (cookie == null) {
                return;
            }
            Cookie newCookie = new Cookie.Builder().name(cookie.name()).value(cookie.value()).domain(destUri.getHost()).build();
            cookieList.add(newCookie);
        }
        HttpUrl destHttpUrl = new HttpUrl.Builder().scheme(scheme).host(destUri.getHost()).build();
        saveFromResponse(destHttpUrl, cookieList);
    }

    /**
     * 解决okhttp3.0中Cookie.java错误地重写equals和hashcode导致cookie重复的问题
     */
    private class CookieWrapper {
        Cookie cookie;

        CookieWrapper(Cookie cookie) {
            this.cookie = cookie;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o != null && o instanceof CookieWrapper) {
                Cookie cookie1 = ((CookieWrapper) o).cookie;
                if (cookie.name().equalsIgnoreCase(cookie1.name()) && (cookie.domain() != null ? cookie.domain().equalsIgnoreCase(cookie1.domain()) :
                        cookie1.domain() == null) && (cookie.path() != null ? cookie.path().equals(cookie1.path()) : cookie1.path() == null)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return cookie.name().toLowerCase(Locale.US).hashCode() + (cookie.domain() == null ? 0 : cookie.domain().toLowerCase(Locale.US).hashCode()) +
                    (cookie.path() == null ? 0 : cookie.path().hashCode());
        }
    }
}
