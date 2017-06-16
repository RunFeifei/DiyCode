package com.dianrong.crnetwork.framework.error;


import com.dianrong.crnetwork.response.RequestException;

public interface ErrorHandler {
    boolean onErrorOccurs(RequestException error);
}
