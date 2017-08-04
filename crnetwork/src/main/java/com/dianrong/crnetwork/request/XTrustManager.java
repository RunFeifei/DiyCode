package com.dianrong.crnetwork.request;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by PengFeifei on 17-8-4.
 */

public class XTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[0];
    }

}
