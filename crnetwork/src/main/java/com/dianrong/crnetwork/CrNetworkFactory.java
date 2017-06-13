package com.dianrong.crnetwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.dianrong.android.common.AppContext;
import com.dianrong.android.common.utils.Log;
import com.dianrong.crnetwork.host.BaseUrlBindHelper;
import com.dianrong.crnetwork.internal.ExtendInterceptor;
import com.dianrong.crnetwork.internal.HeaderInterceptor;
import com.example.crnetwork.BuildConfig;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import util.Strings;

/**
 * <p>
 * Created by yangcheng on 16/3/25.
 */
public class CrNetworkFactory {

    private static final String TAG = CrNetworkFactory.class.getSimpleName();

    private static final long CONNECT_TIMEOUT = 10_000;
    private static final long READ_TIMEOUT = 10_000;
    private static final long WRITE_TIMEOUT = 30_000;
    private static final byte[] retrofitInitLock = new byte[0];
    private static OkHttpClient client;
    private static Retrofit retrofit;
    private static HeaderInterceptor headerInterceptor;
    private static ExtendInterceptor applicationInterceptor;
    private static ExtendInterceptor networkInterceptor;

    private static void initHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 为了调试方便，DEBUG版忽视所有证书问题。
        if (BuildConfig.DEBUG) {
            try {
                final TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        // 解决okhttp3.x的NPE Bug
                        return new java.security.cert.X509Certificate[0];
                    }
                }};

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                builder.sslSocketFactory(sslSocketFactory);
                builder.hostnameVerifier((hostname, session) -> true);

                CrLoggingInterceptor loggingInterceptor = new CrLoggingInterceptor();
                loggingInterceptor.setLevel(CrLoggingInterceptor.Level.BODY);
                builder.addNetworkInterceptor(loggingInterceptor);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 设置缓存大小(只有在服务器返回的Header中的Cache-Control字段maxAge>0才生效 否则需要客户端改写Header see:HeaderInterceptor.class)
        File file = new File(AppContext.getInstance().getCacheDir().getAbsolutePath(), "okhttp");
        builder.cache(new Cache(file, 4 * 1024 * 1024));

        /**
         * addNetworkInterceptor-->网络拦截器，在request和resposne是分别被调用一次
         * addinterceptor-->aplication拦截器，只在response被调用一次
         */
        builder.addInterceptor(getHeaderInterceptor());

        builder.cookieJar(CrOkCookieStore.getInstance());

        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS);

        builder.addInterceptor(getApplicationInterceptor());
        builder.addNetworkInterceptor(getNetworkInterceptor());

        client = builder.build();
    }

    public static final OkHttpClient getClient() {

        if (client == null) {
            initHttpClient();
        }
        return client;
    }

    /**
     * 初始化,并将client和retrofit联系在一起
     *
     * @param baseUrl
     */
    private static void initRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (!Strings.isEmpty(baseUrl) && (baseUrl.startsWith("http://") || baseUrl.startsWith("https://"))) {
            builder.baseUrl(baseUrl);
        }
        builder.addConverterFactory(JacksonConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.client(CrNetworkFactory.getClient());
        retrofit = builder.build();
        Log.i(TAG, "Now: baseUrl is " + getBaseUrl());
    }

    private static Retrofit getRetrofit() {
        if (client == null) {
            initHttpClient();
        }
        synchronized (retrofitInitLock) {
            if (retrofit == null) {
                initRetrofit(BaseUrlBindHelper.getBaseUrl());
            }
        }

        return retrofit;
    }

    public static void resetBaseUrl(@NonNull String baseUrl) {
        if (Strings.isEqual(baseUrl, getBaseUrl())) {
            Log.i(TAG, "baseUrl stays in " + baseUrl);
            return;
        }
        if (Strings.isEmpty(baseUrl) || !baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            throw new IllegalStateException("baseUrl is illegal: " + baseUrl);
        }
        CrOkCookieStore.getInstance().checkWebCookieUpdate(baseUrl);
        rebuildRetrofit(baseUrl);
    }

    /**
     * navite->web 域名切换的同时 需要把cookie同步过去
     * 此时需要重新将CrOkcookieStore重新和OkHttpClient绑定吗??
     */
    private static void rebuildRetrofit(String baseUrl) {
        if (client == null) {
            initHttpClient();
        }
        initRetrofit(baseUrl);
    }

    public static <S> S createService(Class<S> serviceClass) {
        return getRetrofit().create(serviceClass);
    }

    public static void addApplicationInterceptor(Interceptor interceptor) {
        getApplicationInterceptor().addInterceptor(interceptor);
    }

    public static void addNetworkInterceptor(Interceptor interceptor) {
        getNetworkInterceptor().addInterceptor(interceptor);
    }

    private static HeaderInterceptor getHeaderInterceptor() {
        if (headerInterceptor == null) {
            headerInterceptor = new HeaderInterceptor();
        }
        return headerInterceptor;
    }

    private static ExtendInterceptor getApplicationInterceptor() {
        if (applicationInterceptor == null) {
            applicationInterceptor = new ExtendInterceptor();
        }
        return applicationInterceptor;
    }

    public static ExtendInterceptor getNetworkInterceptor() {
        if (networkInterceptor == null) {
            networkInterceptor = new ExtendInterceptor();
        }
        return networkInterceptor;
    }

    public static String getUserAgent() {
        return getHeaderInterceptor().getUserAgent();
    }

    /**
     * 设置UserAgent，如果不想覆盖当前的UserAgent，可以先通过getUserAgent()获取，再加上新增的UserAgent
     *
     * @param userAgent
     */
    public static void setUserAgent(String userAgent) {
        getHeaderInterceptor().setUserAgent(userAgent);
    }

    public static void addHeader(String key, String value) {
        if (value == null) {
            return;
        }
        getHeaderInterceptor().addHeader(key, value);
    }

    public static String getBaseUrl() {
        if (retrofit == null || client == null) {
            return null;
        }
        return retrofit.baseUrl().toString();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = cm.getAllNetworkInfo();
        for (NetworkInfo info : infos) {
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}