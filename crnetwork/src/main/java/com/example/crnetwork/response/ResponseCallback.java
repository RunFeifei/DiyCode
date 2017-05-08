package com.example.crnetwork.response;

import com.example.crnetwork.dataformat.Entity;
import com.example.crnetwork.error.ErrorCode;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by PengFeifei on 17-4-20.
 */

public abstract class ResponseCallback<T extends Entity> implements retrofit2.Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        HttpUrl url;
        try {
            url = call.request().url();
        } catch (NullPointerException e) {
            url = HttpUrl.parse("https://www.null.com/");
        }

        if (response == null) {
            throw new RequestException(url, ErrorCode.RESPONSE_NULL_ERR, "response is null");
        }
        if (!response.isSuccessful()) {
            throw new RequestException(url, response.code(), "response is failed");
        }
        T responseBody = response.body();
        if (responseBody == null) {
            throw new RequestException(url, response.code(), "responseBody is null");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        throw new RequestException(call.request().url(), ErrorCode.NETWORK_ERR, t);
    }
}
