package com.fei.crnetwork.framework.adapter;

import com.fei.crnetwork.error.ErrorCode;
import com.fei.crnetwork.response.RequestException;

import okhttp3.HttpUrl;

/**
 * Created by PengFeifei on 17-6-19.
 */

public class ExceptionAdapter {

    public static RequestException toRequestException(Throwable e, HttpUrl httpUrl) {
        boolean isdefinedException = e instanceof RequestException;
        if (isdefinedException) {
            return (RequestException) e;
        } else {
            return new RequestException(httpUrl, ErrorCode.UNKNOWN_ERR, e);
        }
    }
}
