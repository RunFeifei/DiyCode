package com.dianrong.crnetwork.internal;

import com.dianrong.crnetwork.host.BaseUrlBinder;
import com.feifei.common.MultiApplication;
import com.feifei.common.utils.AppInfo;
import com.feifei.common.utils.StorageUtil;
import com.google.gson.JsonObject;

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
     * userAgent是一个特殊字符串头,使得服务器能够识别客户端
     */
    public HeaderInterceptor() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("platform", "Android");
        jsonObject.addProperty("systemVersion", AppInfo.getSystemVersion());
        jsonObject.addProperty("appName", AppInfo.getAppName(MultiApplication.getContext()).toString());
        jsonObject.addProperty("versionCode",AppInfo.getVersionCode(MultiApplication.getContext()));
        jsonObject.addProperty("versionName",AppInfo.getVersionName(MultiApplication.getContext()));
        jsonObject.addProperty("clientType", AppInfo.getClientType());
        jsonObject.addProperty("ChannelId", AppInfo.getChannelName());
        userAgent = jsonObject.toString();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        requestBuilder.header("User-Agent", userAgent);
        //requestBuilder.header("userinfo", userId);
        requestBuilder.header("X-SL-UUID", StorageUtil.getSlUUID());
        requestBuilder.header("IMEI", AppInfo.getImei());
        requestBuilder.header("Referer", BaseUrlBinder.getBaseUrl());
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

    public void putHeader(String key, String value) {
        headers.put(key, value);
    }
}