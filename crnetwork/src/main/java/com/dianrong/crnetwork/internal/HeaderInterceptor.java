package com.dianrong.crnetwork.internal;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.utils.ContextUtils;
import com.dianrong.android.common.utils.DRPreferences;
import com.dianrong.android.user.UserStatus;
import com.dianrong.crnetwork.host.BaseUrlBindHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by PengFeifei on 17-6-13.
 * 缓存参考:http://blog.csdn.net/copy_yuan/article/details/51524907
 */

public class HeaderInterceptor implements Interceptor {

    private String userAgent = "";
    private HashMap<String, String> headers = new HashMap<>();

    /**
     * userAgent是一个特殊字符串头
     * 使得服务器能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本...
     */
    public HeaderInterceptor() {
        userAgent = "Android/" + ContextUtils.getSystemVersion()
                + " " + ContextUtils.getAppName() + "/" + ContextUtils.getVersionCode(AppContext.getInstance())
                + " ClientType/" + ContextUtils.getClientType()
                + " ChannelId/" + ContextUtils.getChannelName();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        requestBuilder.header("User-Agent", userAgent);
        if (UserStatus.isLoggedIn() && UserStatus.getUser() != null && UserStatus.getUser().getAid() != null) {
            requestBuilder.header("X-SL-Username", UserStatus.getUser().getAid());
        }
        requestBuilder.header("X-SL-UUID", DRPreferences.getSlUUID());
        requestBuilder.header("IMEI", ContextUtils.getImei());
        requestBuilder.header("Referer", BaseUrlBindHelper.getBaseUrl());
        requestBuilder.removeHeader("Pragma");//在HTTP1.0中Pragma: no-cache,删除旧的
        //requestBuilder.removeHeader("Cache-Control");//删除旧的
        requestBuilder.header("Cache-Control", "public, max-age=" + 3600 * 24);//强制复写为24h

        for (Map.Entry<String, String> item : headers.entrySet()) {
            requestBuilder.header(item.getKey(), item.getValue());
        }

        Response response = chain.proceed(requestBuilder.build());
        return response;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}