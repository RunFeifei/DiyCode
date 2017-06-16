package com.dianrong.crnetwork.framework;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.dianrong.crnetwork.dataformat.Entity;
import com.dianrong.crnetwork.framework.error.ErrorHandler;
import com.dianrong.crnetwork.framework.requests.Requests;
import com.dianrong.crnetwork.framework.view.IBaseView;
import com.trello.rxlifecycle.components.ActivityLifecycleProvider;
import com.trello.rxlifecycle.components.FragmentLifecycleProvider;

import retrofit2.Call;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by PengFeifei on 17-6-19.
 */

public class RequestAgent<T extends Entity> {

    private final IBaseView baseView;
    private FragmentLifecycleProvider fragmentLifecycle;
    private ActivityLifecycleProvider activityLifecycle;
    private ErrorHandler errorHandler;
    private Call<T> call;
    private Requests<T> requests;

    public RequestAgent(IBaseView baseView) {
        this.baseView = baseView;
        if (baseView instanceof Fragment) {
            this.fragmentLifecycle = (FragmentLifecycleProvider) baseView;
            return;
        }
        if (baseView instanceof ActivityCompat) {
            this.activityLifecycle = (ActivityLifecycleProvider) baseView;
            return;
        }
        throw new RuntimeException("activity of fragment must implement IBaseView & one of (FragmentLifecycleProvider or ActivityLifecycleProvider)");
    }

    public RequestAgent bindErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    private ObservableHandler getObservableHandler() {
        if (activityLifecycle == null) {
            return new ObservableHandler(baseView, fragmentLifecycle);
        }
        return new ObservableHandler(baseView, activityLifecycle);
    }

    /***************************************Single Request Part***************************************/
    public RequestAgent bindCall(@NonNull Call<T> call) {
        this.call = call;
        return this;
    }

    public void singleRequestData(Action1<Entity> onData) {
        final ErrorHandler errorHandlerWork = errorHandler;
        getObservableHandler().getContentData(call.clone(), onData, errorHandlerWork);
    }

    public Observable<Entity> singleRequestObservable() {
        return getObservableHandler().getContentObservable(call.clone());
    }

    /***************************************Multiple Requests Part***************************************/

    public RequestAgent bindRequests(@NonNull Requests<T> requests) {
        this.requests = requests;
        return this;
    }

    public void multipleRequestData(Action1<Entity> onData) {
        final Requests<T> requestsWork = requests;
        final ErrorHandler errorHandlerWork = errorHandler;
        getObservableHandler().getRequestsData(requestsWork, onData, errorHandlerWork);
    }

    public void multipleRequestObservable() {
        final Requests<T> requestsWork = requests;
        getObservableHandler().getRequestsObservable(requestsWork);
    }


}