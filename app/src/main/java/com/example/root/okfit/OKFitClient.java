package com.example.root.okfit;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PengFeifei on 17-4-17.
 * https://www-demo.dianrong.com/feapi/errors
 */

public class OKFitClient {


    private static Retrofit.Builder getRetrofitBuilder() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://www-demo.dianrong.com")
                .addConverterFactory(GsonConverterFactory.create());
        return retrofitBuilder;
    }

    private static OkHttpClient.Builder getOkBuilder() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
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
                    return new java.security.cert.X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okBuilder.sslSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return okBuilder;
    }


    public static <T> T getService(Class<T> serviceClass) {
        Retrofit retrofit = getRetrofitBuilder().client(getOkBuilder().build()).build();
        return retrofit.create(serviceClass);
    }


}
