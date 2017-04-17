package com.example.crnetwork.response;

import okhttp3.HttpUrl;

/**
 * Created by PengFeifei on 17-4-21.
 */

public class RequestException extends RuntimeException {

    public RequestException(HttpUrl url, int code, Throwable cause) {
        super(cause);
    }


    public RequestException(HttpUrl url, int businessCode) {
        super("URL: " + url + " businessCode: " + businessCode);
    }

    public RequestException(HttpUrl url, int businessCode, int httpCode) {
        super("URL: " + url + " businessCode: " + businessCode + " HttpCode: " + httpCode);
    }

    public RequestException(HttpUrl url, int businessCode, String dianrongErr) {
        super("URL: " + url + " businessCode: " + businessCode + " dianrongErr: " + dianrongErr);
    }
}