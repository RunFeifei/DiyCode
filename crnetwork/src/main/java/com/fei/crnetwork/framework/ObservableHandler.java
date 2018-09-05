package com.fei.crnetwork.framework;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.fei.crnetwork.framework.adapter.SchedulerAdapter;
import com.fei.crnetwork.framework.error.ErrorHandler;
import com.fei.crnetwork.framework.requests.Requests;
import com.fei.crnetwork.framework.subscriber.DefaultSubscriber;
import com.fei.crnetwork.framework.view.IBaseView;
import com.fei.crnetwork.response.RequestException;
import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.ActivityLifecycleProvider;
import com.trello.rxlifecycle.components.FragmentLifecycleProvider;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.io.Serializable;

import okhttp3.HttpUrl;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by PengFeifei on 17-4-20.
 */

public class ObservableHandler<T, Data extends Serializable> {

    private IBaseView baseView;
    private FragmentLifecycleProvider fragmentLifecycle;
    private ActivityLifecycleProvider activityLifecycle;
    /**
     * single Request
     **/
    private static HttpUrl httpUrl;

    public ObservableHandler(IBaseView baseView) {
        this.baseView = baseView;
        if (baseView instanceof FragmentLifecycleProvider) {
            this.fragmentLifecycle = (FragmentLifecycleProvider) baseView;
            return;
        }
        if (baseView instanceof ActivityLifecycleProvider) {
            this.activityLifecycle = (ActivityLifecycleProvider) baseView;
            return;
        }
        throw new RuntimeException("activity of fragment must implement IBaseView & one of (FragmentLifecycleProvider or ActivityLifecycleProvider)");
    }

    /***************************************Single Request Part***************************************/
    public void getData(Observable<T> observable, @NonNull Action1<Data> onData, ErrorHandler errorHandler) {
        getObservable(observable)
                .subscribe(new DefaultSubscriber<Data>(baseView, errorHandler) {
                    @Override
                    public void onHandleData(Data t) {
                        onData.call(t);
                    }
                });
    }

    public Observable<Data> getObservable(Observable<T> observable) {
        checkMainThread();
        return observable
                .compose(bindUntilEvent())
                .observeOn(SchedulerAdapter.uiScheduler)
                .subscribeOn(SchedulerAdapter.backScheduler)
                .filter(filterHttpResponse())
                .map(map2RootData());
    }

    /***************************************Multiple Requests Part***************************************/

    /**
     * 用DrResponse解析数据!才能同步HttpUrl
     * ResponseCallback or ResponseHandler
     */
    public void getRequestsData(Requests<Data> requests, @NonNull Action1<Data> onData, ErrorHandler errorHandler) {
        getRequestsObservable(requests)
                .subscribe(new DefaultSubscriber<Data>(baseView, errorHandler) {
                    @Override
                    public void onHandleData(Data t) {
                        onData.call(t);
                    }
                });
    }

    /**
     * 同步依次执行多个网络请求时,不能拦截http的Code
     */
    public Observable<Data> getRequestsObservable(Requests<Data> requests) {
        checkMainThread();
        return Observable
                .create(new Observable.OnSubscribe<Data>() {
                    @Override
                    public void call(Subscriber<? super Data> subscriber) {
                        subscriber.onStart();
                        subscriber.onNext(requests.onRequests());
                        subscriber.onCompleted();
                    }
                })
                .compose(bindRequestsUntilEvent())
                .subscribeOn(SchedulerAdapter.backScheduler)
                .observeOn(SchedulerAdapter.uiScheduler);
    }


    /***************************************Utils Part***************************************/
    private void checkMainThread() {
        setHttpUrl(null);
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalStateException("Must be invoked in Main thread");
        }
    }

    private Func1<T, Data> map2RootData() {
        return new Func1<T, Data>() {
            @Override
            public Data call(T t) {
                if (t != null && t instanceof Response) {
                    Response<T> response = (Response<T>) t;
                    return (Data) response.body();
                }
                return null;
            }
        };
    }

    private Func1<T, Boolean> filterHttpResponse() {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                if (t != null && t instanceof Response) {
                    Response<T> response = (Response<T>) t;
                    ObservableHandler.httpUrl = response.raw().request().url();
                    if (response.code() != 200) {
                        int code = response.code();
                        throw new RequestException(getHttpUrl(), code, "response code--> " + code + " != 200");
                    }
                    return response.isSuccessful();
                }
                return true;
            }
        };
    }

    /**
     * 页面不可见自动解除订阅
     */
    private Observable.Transformer<T, T> bindUntilEvent() {
        if (fragmentLifecycle == null && activityLifecycle == null) {
            throw new IllegalStateException("bindUntilEvent params NPE");
        }

        if (fragmentLifecycle != null && activityLifecycle != null) {
            throw new IllegalStateException("fragment & activity both initialized");
        }
        if (activityLifecycle != null) {
            return activityLifecycle.bindUntilEvent(ActivityEvent.PAUSE);
        }
        return fragmentLifecycle.bindUntilEvent(FragmentEvent.PAUSE);
    }

    private Observable.Transformer<Data, Data> bindRequestsUntilEvent() {
        if (fragmentLifecycle == null && activityLifecycle == null) {
            throw new IllegalStateException("bindUntilEvent params NPE");
        }

        if (fragmentLifecycle != null && activityLifecycle != null) {
            throw new IllegalStateException("fragment & activity both initialized");
        }
        if (activityLifecycle != null) {
            return activityLifecycle.bindUntilEvent(ActivityEvent.PAUSE);
        }
        return fragmentLifecycle.bindUntilEvent(FragmentEvent.PAUSE);
    }

    /***************************************getter or setter***************************************/

    public static HttpUrl getHttpUrl() {
        return ObservableHandler.httpUrl == null ? RequestException.REQUEST_UNKNOWN : ObservableHandler.httpUrl;
    }

    public static HttpUrl setHttpUrl(HttpUrl httpUrl) {
        return ObservableHandler.httpUrl = null;
    }


}
