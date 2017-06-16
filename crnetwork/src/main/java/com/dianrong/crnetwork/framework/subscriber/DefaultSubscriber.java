package com.dianrong.crnetwork.framework.subscriber;

import android.support.annotation.NonNull;

import com.dianrong.crnetwork.framework.adapter.ExceptionAdapter;
import com.dianrong.crnetwork.framework.error.ErrorHandler;
import com.dianrong.crnetwork.framework.view.IBaseView;
import com.dianrong.crnetwork.response.RequestException;

import okhttp3.HttpUrl;

/**
 * Created by PengFeifei on 17-6-19.
 */

public abstract class DefaultSubscriber<T> extends BaseSubscriber<T> {

    private ErrorHandler errorHandler;
    private HttpUrl httpUrl;
    private IBaseView baseView;

    public DefaultSubscriber(@NonNull IBaseView baseView, HttpUrl httpUrl, ErrorHandler errorHandler) {
        super(baseView, httpUrl);
        this.errorHandler = errorHandler;
        this.httpUrl = httpUrl;
        this.baseView = baseView;
    }

    @Override
    public void onError(Throwable e) {
        try {
            RequestException requestException = ExceptionAdapter.toRequestException(e, httpUrl);
            if (errorHandler != null && errorHandler.onErrorOccurs(requestException)) {
                return;
            }
            super.onError(e);
        } catch (Exception e1) {
            baseView.onRequestError(ExceptionAdapter.toRequestException(e, httpUrl));
        }
    }

}
