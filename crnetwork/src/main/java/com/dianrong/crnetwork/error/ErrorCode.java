package com.dianrong.crnetwork.error;

/**
 * Created by PengFeifei on 17-4-21.
 */

public @interface ErrorCode {

    int NETWORK_ERR = 100;
    int RESPONSE_NULL_ERR = 101;
    int REQUEST_TIMEOUT_ERR = 102;
    int UNKNOWN_ERR = 106;
}