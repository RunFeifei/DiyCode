package com.fei.crnetwork.framework.subscriber;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fei.crnetwork.framework.ObservableHandler;
import com.fei.crnetwork.framework.adapter.ExceptionAdapter;
import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.framework.view.loading.Loading;
import com.fei.crnetwork.framework.view.loading.OnLoadingDismissListener;

import rx.Subscriber;


/**
 * Created by PengFeifei on 17-6-14.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> implements OnLoadingDismissListener {

    private IBaseView baseView;
    private boolean cancelled;
    private Loading loading;

    public BaseSubscriber(@NonNull IBaseView baseView) {
        this.baseView = baseView;
    }


    public abstract void onHandleData(T t);

    @Override
    public void onStart() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onStart");
        super.onStart();
        loading = baseView.onLoadIng();
        boolean onStart = baseView.onRequestStart();
        if (!onStart) {
            if (loading != null) {
                loading.showLoading();
                loading.setOnDismissListener(this);
            }
        }

    }

    @Override
    public void onNext(T t) {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onNext");
        if (!cancelled) {
            baseView.onRequestEnd();
            if (loading != null) {
                loading.dismissLoading();
            }
            cancelled = false;
        }
        if (baseView.onPageVisible() && t != null) {
            onHandleData(t);
        }
    }

    @Override
    public void onCompleted() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onCompleted");
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onError(Throwable e) {
        onError(e, true);
    }

    protected void onError(Throwable e, boolean consumedException) {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onError");
        Log.e("BaseSubscriber-->", e.getMessage());
        if (!cancelled) {
            if (loading != null) {
                loading.dismissLoading();
            }
            cancelled = false;
        }
        if (!consumedException) {
            baseView.onRequestError(ExceptionAdapter.toRequestException(e, ObservableHandler.getHttpUrl()));
        }
        ObservableHandler.setHttpUrl(null);
    }

    @Override
    public void onDialogCancelled() {
        Log.d("BaseSubscriber-->", "-------------------------------------------------onDialogCancelled");
        cancelled = true;
        baseView.onRequestCacell();
        unsubscribe();
        ObservableHandler.setHttpUrl(null);
    }



}
