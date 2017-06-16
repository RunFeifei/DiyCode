package com.dianrong.crnetwork.framework.adapter;

import com.dianrong.crnetwork.error.ErrorCode;
import com.dianrong.crnetwork.response.RequestException;

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
            if (httpUrl == null) {
                httpUrl = RequestException.ILLEGAL_URL;
            }
            return new RequestException(httpUrl, ErrorCode.UNKNOWN_ERR, e);
        }
    }
}
