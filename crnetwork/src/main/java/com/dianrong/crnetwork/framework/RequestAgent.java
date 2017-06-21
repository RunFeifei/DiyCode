package com.dianrong.crnetwork.framework;

import android.support.annotation.NonNull;

import com.dianrong.crnetwork.dataformat.Entity;
import com.dianrong.crnetwork.framework.error.ErrorHandler;
import com.dianrong.crnetwork.framework.requests.Requests;
import com.dianrong.crnetwork.framework.view.IBaseView;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by PengFeifei on 17-6-19.
 */

public class RequestAgent<T extends Entity> {

    private final IBaseView baseView;
    private ErrorHandler errorHandler;
    private Requests<T> requests;
    private Observable observable;

    public RequestAgent(@NonNull IBaseView baseView) {
        this.baseView = baseView;
    }

    /**
     * 只对回调方式获取Data的方式有效,onData() or onRequestData()
     */
    public RequestAgent<T> bindErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    private ObservableHandler getObservableHandler() {
        return new ObservableHandler(baseView);
    }

    public void onData(Action1<T> onData) {
        final Observable<T> observable = this.observable;
        final Requests<T> requestsWork = requests;
        if (observable != null && requestsWork != null) {
            throw new RuntimeException("can not do requets & observable");
        }
        if (observable == null && requestsWork == null) {
            throw new RuntimeException("not notify requets or observable");
        }
        final ErrorHandler errorHandlerWork = errorHandler;
        if (observable != null) {
            getObservableHandler().getData(observable, onData, errorHandlerWork);
            return;
        }
        getObservableHandler().getRequestsData(requestsWork, onData, errorHandlerWork);
    }

    public Observable<T> onObservable() {
        final Observable<T> observable = this.observable;
        final Requests<T> requestsWork = requests;
        if (observable != null && requestsWork != null) {
            throw new RuntimeException("can not do requets & observable");
        }
        if (observable == null && requestsWork == null) {
            throw new RuntimeException("not notify requets or observable");
        }
        if (observable != null) {
            return getObservableHandler().getObservable(observable);
        }
        return getObservableHandler().getRequestsObservable(requestsWork).asObservable();
    }

    /***************************************Single Request Part***************************************/

    public <R extends Response> RequestAgent<T> bindObservable(Observable<R> observable) {
        this.observable = observable;
        return this;
    }

    /***************************************Multiple Requests Part***************************************/

    public RequestAgent<T> bindRequests(@NonNull Requests<T> requests) {
        this.requests = requests;
        return this;
    }

}