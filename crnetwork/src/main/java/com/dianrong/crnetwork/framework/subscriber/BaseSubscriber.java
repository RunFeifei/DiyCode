package com.dianrong.crnetwork.framework.subscriber;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.dianrong.crnetwork.framework.adapter.ExceptionAdapter;
import com.dianrong.crnetwork.framework.view.IBaseView;
import com.dianrong.crnetwork.framework.view.loading.LoadingDialog;

import okhttp3.HttpUrl;
import rx.Subscriber;

/**
 * Created by PengFeifei on 17-6-14.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> implements Dialog.OnDismissListener {

    private IBaseView baseView;
    private HttpUrl httpUrl;

    public BaseSubscriber(@NonNull IBaseView baseView, HttpUrl httpUrl) {
        this.baseView = baseView;
        this.httpUrl = httpUrl;
    }

    @Override
    public void onNext(T t) {
        if (baseView.onPageVisible() && t != null) {
            onHandleData(t);
            this.unsubscribe();
        } else {
            this.unsubscribe();
        }
    }

    public abstract void onHandleData(T t);

    @Override
    public void onStart() {
        super.onStart();
        LoadingDialog.showLoading(baseView.onLoading()).setOnDismissListener(this);
        baseView.onRequestStart();
    }

    @Override
    public void onCompleted() {
        baseView.onRequestEnd();
    }

    @Override
    public void onError(Throwable e) {
        baseView.onRequestError(ExceptionAdapter.toRequestException(e, httpUrl));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        unsubscribe();
    }
}
