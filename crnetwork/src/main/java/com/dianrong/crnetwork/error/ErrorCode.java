package com.dianrong.crnetwork.error;

/**
 * Created by PengFeifei on 17-4-21.
 */

public @interface ErrorCode {

    int NETWORK_ERR = 100;
    int RESPONSE_NULL_ERR = 101;
    int DR_CAST_ERR = 102;
    int DR_RELOGIN_ERR = 103;
    int DR_INTERCEPTION_LOGIN_ERR = 104;
    int UNKNOWN_ERR = 105;

    enum DrResultCode {
        //服务器返回的错误码
        Success,
        Login,
        AuthFirst,
        Error,
        ServiceDisabled,
        SessionTimeout,
        LenderNotAMember,
        //其他错误
        Timeout,
        NoConnection,
        Exception,
        MelformedResponse,
        Unknown,
        //token过期
        TokenIsExpired;

        private DrResultCode() {
        }
    }

}