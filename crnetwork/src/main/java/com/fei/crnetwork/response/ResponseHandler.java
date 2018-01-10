package com.fei.crnetwork.response;

import com.fei.crnetwork.error.ErrorCode;
import com.fei.crnetwork.framework.ObservableHandler;

import java.io.Serializable;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by PengFeifei on 17-4-20.
 */

public class ResponseHandler {

    private static final String TAG = ResponseHandler.class.getSimpleName();

    /**
     * 同步方法
     * 勿在主线程执行此方法
     */
    public static <T extends Serializable> Response<T> getSyncResponse(Call<T> call) {
        HttpUrl url;
        try {
            url = call.request().url();
        } catch (NullPointerException e) {
            url = RequestException.REQUEST_UNKNOWN;
        }
        /*not good!!! 不利于解耦*/
        ObservableHandler.setHttpUrl(url);

        Response<T> response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            throw new RequestException(url, ErrorCode.NETWORK_ERR, e);
        }

        if (response == null) {
            throw new RequestException(url, ErrorCode.RESPONSE_NULL_ERR, "response is null");
        }
        if (!response.isSuccessful()) {
            throw new RequestException(url, response.code(), "response is failed");
        }
        return response;
    }


    /**
     * 异步方法
     */
    public static <T extends Serializable> void getAsyncResponse(Call<T> call, ResponseCallback<T> callback) {
        call.enqueue(callback);
    }

}
