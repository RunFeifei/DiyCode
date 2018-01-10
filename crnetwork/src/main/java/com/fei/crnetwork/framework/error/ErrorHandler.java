package com.fei.crnetwork.framework.error;


import com.fei.crnetwork.response.RequestException;

public interface ErrorHandler {
    boolean onErrorOccurs(RequestException error);
}
